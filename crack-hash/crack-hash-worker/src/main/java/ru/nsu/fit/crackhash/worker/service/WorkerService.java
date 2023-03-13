package ru.nsu.fit.crackhash.worker.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nsu.fit.crackhash.worker.dto.request.CrackHashRequest;
import ru.nsu.fit.crackhash.worker.dto.response.WorkerResponse;
import ru.nsu.fit.crackhash.worker.service.exception.ManagerIsNotAvailableException;

import java.util.List;
import java.util.Objects;

@Service
public class WorkerService {
    private final CrackHashService crackHashService;

    private final EurekaClient discoveryClient;

    @Autowired
    public WorkerService(CrackHashService crackHashService, EurekaClient discoveryClient) {
        this.crackHashService = crackHashService;
        this.discoveryClient = discoveryClient;
    }

    @Async
    public void crackHash(CrackHashRequest crackHashRequest) throws ManagerIsNotAvailableException {
        WorkerResponse requestBody = crackHashService.crackHash(crackHashRequest);

        Application managerApplication = discoveryClient.getApplication("crack-hash-manager");
        if (Objects.isNull(managerApplication)) {
            throw new ManagerIsNotAvailableException();
        }

        List<InstanceInfo> managerInstances = managerApplication.getInstances()
                                                                .stream()
                                                                .filter(instanceInfo -> instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP)
                                                                .toList();

        if (managerInstances.isEmpty()) {
            throw new ManagerIsNotAvailableException();
        }

        InstanceInfo manager = managerInstances.get(0);

        String requestUrl = "internal/api/manager/hash/crack/request";

        WebClient
                .create(manager.getHomePageUrl())
                .patch()
                .uri(requestUrl)
                .contentType(MediaType.APPLICATION_XML)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
