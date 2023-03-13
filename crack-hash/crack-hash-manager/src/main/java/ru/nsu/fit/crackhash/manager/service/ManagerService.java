package ru.nsu.fit.crackhash.manager.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nsu.fit.crackhash.manager.dto.request.CrackHashManagerRequest;
import ru.nsu.fit.crackhash.manager.dto.request.CrackHashRequest;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashResponse;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashStatus;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashStatusResponse;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashWorkerResponse;
import ru.nsu.fit.crackhash.manager.service.exception.NoWorkersAvailableException;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ManagerService {
    private final List<String> alphabet = Arrays.stream("abcdefghijklmnopqrstuvwxyz1234567890".split("")).toList();

    private final Map<UUID, CrackHashStatusResponse> results = new ConcurrentHashMap<>();

    private final Map<UUID, List<String>> partialResults = new ConcurrentHashMap<>();

    private final Map<UUID, AtomicInteger> numberOfJobs = new ConcurrentHashMap<>();

    private final Map<UUID, Future<?>> timeouts = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executorService;

    private final EurekaClient discoveryClient;

    @Value("${crack-hash.timeout-in-sec:60}")
    private Long timeout;

    @Autowired
    public ManagerService(EurekaClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    public CrackHashResponse crackHash(CrackHashRequest crackHashRequest) throws NoWorkersAvailableException {
        UUID requestId = UUID.randomUUID();

        List<InstanceInfo> workerInstances = getWorkerInstances();

        numberOfJobs.put(requestId, new AtomicInteger(workerInstances.size()));
        partialResults.put(requestId, Collections.synchronizedList(new ArrayList<>()));
        results.put(requestId, CrackHashStatusResponse.builder().status(CrackHashStatus.IN_PROGRESS).build());

        log.info("Job #{}. Number of workers: {}", requestId, workerInstances.size());

        sendTaskToWorkers(requestId, workerInstances, crackHashRequest);

        setTimeout(requestId, timeout);

        return CrackHashResponse.builder()
                                .requestId(requestId)
                                .build();
    }

    public void receiveWorkerResponse(CrackHashWorkerResponse crackHashWorkerResponse) {
        UUID requestId = UUID.fromString(crackHashWorkerResponse.getRequestId());

        log.info("Receiving job #{} from worker #{}", requestId, crackHashWorkerResponse.getPartNumber());

        if (!crackHashWorkerResponse.getAnswers().getWords().isEmpty()) {
            partialResults.computeIfPresent(requestId, (key, value) -> {
                value.addAll(crackHashWorkerResponse.getAnswers().getWords());
                return value;
            });
        }

        AtomicInteger currentNumberOfJobsAtomic = numberOfJobs.get(requestId);
        if (Objects.isNull(currentNumberOfJobsAtomic)) {
            return;
        }

        int currentNumberOfJobs = currentNumberOfJobsAtomic.decrementAndGet();
        if (currentNumberOfJobs == 0) {
            timeouts.get(requestId).cancel(true);

            results.computeIfPresent(requestId, (key, value) -> {
                value.setStatus(CrackHashStatus.READY);
                value.setData(new HashSet<>(partialResults.get(requestId)));
                return value;
            });

            clearTempData(requestId);
        }
    }

    public CrackHashStatusResponse getStatus(UUID requestId) {
        return results.getOrDefault(requestId, CrackHashStatusResponse.builder()
                                                                      .status(CrackHashStatus.UNKNOWN_JOB)
                                                                      .build());
    }

    private List<InstanceInfo> getWorkerInstances() throws NoWorkersAvailableException {
        Application workerApplication = discoveryClient.getApplication("crack-hash-worker");
        if (Objects.isNull(workerApplication)) {
            throw new NoWorkersAvailableException();
        }

        List<InstanceInfo> workerInstances = workerApplication.getInstances()
                                                              .stream()
                                                              .filter(instanceInfo -> instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP)
                                                              .toList();

        if (workerInstances.isEmpty()) {
            throw new NoWorkersAvailableException();
        }

        return workerInstances;
    }

    private void sendTaskToWorkers(UUID requestId, List<InstanceInfo> workerInstances, CrackHashRequest crackHashRequest) {
        for (int partNumber = 0; partNumber < workerInstances.size(); ++partNumber) {
            InstanceInfo instanceInfo = workerInstances.get(partNumber);

            log.info("Sending job #{} to worker #{}: {}", requestId, partNumber, instanceInfo.getHomePageUrl());

            String requestUrl = "internal/api/worker/hash/crack/task";

            CrackHashManagerRequest requestBody = CrackHashManagerRequest.builder()
                                                                         .requestId(requestId.toString())
                                                                         .alphabet(new CrackHashManagerRequest.Alphabet(alphabet))
                                                                         .hash(crackHashRequest.getHash())
                                                                         .maxLength(crackHashRequest.getMaxLength())
                                                                         .partNumber(partNumber)
                                                                         .partCount(workerInstances.size())
                                                                         .build();

            WebClient.create(instanceInfo.getHomePageUrl())
                     .post()
                     .uri(requestUrl)
                     .contentType(MediaType.APPLICATION_XML)
                     .bodyValue(requestBody)
                     .retrieve()
                     .bodyToMono(Void.class)
                     .subscribe();
        }
    }

    private void setTimeout(UUID requestId, long delay) {
        timeouts.put(requestId, executorService.schedule(() -> {
            results.get(requestId).setStatus(CrackHashStatus.ERROR);
            clearTempData(requestId);
        }, delay, TimeUnit.SECONDS));
    }

    private void clearTempData(UUID requestId) {
        timeouts.remove(requestId);
        partialResults.remove(requestId);
        numberOfJobs.remove(requestId);
    }
}
