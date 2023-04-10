package ru.nsu.fit.crackhash.manager.queue.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.manager.exception.RequestNotFoundException;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerResultResponse;
import ru.nsu.fit.crackhash.manager.service.RequestService;

@Slf4j
@Service
public class WorkerResultConsumer {
    private final RequestService requestService;

    public WorkerResultConsumer(RequestService requestService) {
        this.requestService = requestService;
    }

    @RabbitListener(queues = "${crack-hash.rabbitmq.queue.manager}")
    public void consumeWorkerResult(WorkerResultResponse workerResult) throws RequestNotFoundException {
        log.info("Request #{}. Receiving completed part #{}", workerResult.getRequestId(), workerResult.getPartNumber());

        requestService.addWorkerResult(workerResult);
    }
}
