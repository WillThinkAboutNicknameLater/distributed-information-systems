package ru.nsu.fit.crackhash.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.manager.dto.request.CrackHashRequest;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashResponse;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashStatusResponse;
import ru.nsu.fit.crackhash.manager.exception.RequestNotFoundException;
import ru.nsu.fit.crackhash.manager.mapper.TaskMapper;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerTaskRequest;
import ru.nsu.fit.crackhash.manager.queue.producer.WorkerTaskProducer;
import ru.nsu.fit.crackhash.manager.storage.model.DeferredTask;
import ru.nsu.fit.crackhash.manager.storage.model.Request;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HashService {
    private final WorkerTaskProducer workerTaskProducer;

    private final RequestService requestService;

    private final DeferredTaskService deferredTaskService;

    private final TaskMapper taskMapper;

    private final Set<String> alphabet = Arrays.stream("abcdefghijklmnopqrstuvwxyz1234567890".split("")).collect(Collectors.toSet());

    @Value("${crack-hash.number-of-task-parts}")
    private int numberOfTaskParts;

    public HashService(WorkerTaskProducer workerTaskProducer,
                       RequestService requestService,
                       DeferredTaskService deferredTaskService,
                       TaskMapper taskMapper) {
        this.workerTaskProducer = workerTaskProducer;
        this.requestService = requestService;
        this.deferredTaskService = deferredTaskService;
        this.taskMapper = taskMapper;
    }

    public CrackHashResponse crackHash(CrackHashRequest crackHashRequest) throws RequestNotFoundException {
        Request request = requestService.saveRequest(Request.builder()
                                                            .partCount(numberOfTaskParts)
                                                            .build());

        UUID requestId = request.getId();

        sendTaskToWorkers(requestId, crackHashRequest);

        return CrackHashResponse.builder()
                                .requestId(requestId)
                                .build();
    }

    public CrackHashStatusResponse getStatus(UUID requestId) throws RequestNotFoundException {
        Request request = requestService.getRequest(requestId);

        return CrackHashStatusResponse.builder()
                                      .status(request.getStatus())
                                      .data(request.collectAllResults())
                                      .build();
    }

    private void sendTaskToWorkers(UUID requestId, CrackHashRequest crackHashRequest) {
        log.info("Request #{}. Number of task parts: {}", requestId, numberOfTaskParts);

        for (int partNumber = 0; partNumber < numberOfTaskParts; ++partNumber) {
            WorkerTaskRequest workerTaskRequest = WorkerTaskRequest.builder()
                                                                   .requestId(requestId)
                                                                   .alphabet(new WorkerTaskRequest.Alphabet(alphabet))
                                                                   .hash(crackHashRequest.getHash())
                                                                   .maxLength(crackHashRequest.getMaxLength())
                                                                   .partNumber(partNumber)
                                                                   .partCount(numberOfTaskParts)
                                                                   .build();

            try {
                workerTaskProducer.sendTask(workerTaskRequest);
            } catch (AmqpException e) {
                log.info("Request #{}. Deferring part #{}", requestId, partNumber);

                DeferredTask deferredTask = taskMapper.map(workerTaskRequest);
                deferredTaskService.deferTask(deferredTask);
            }
        }
    }
}
