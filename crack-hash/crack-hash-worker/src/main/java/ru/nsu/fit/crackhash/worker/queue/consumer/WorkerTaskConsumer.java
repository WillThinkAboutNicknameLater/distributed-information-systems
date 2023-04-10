package ru.nsu.fit.crackhash.worker.queue.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.worker.queue.message.WorkerResultResponse;
import ru.nsu.fit.crackhash.worker.queue.message.WorkerTaskRequest;
import ru.nsu.fit.crackhash.worker.queue.producer.WorkerResultProducer;
import ru.nsu.fit.crackhash.worker.service.CrackHashService;

@Slf4j
@Service
public class WorkerTaskConsumer {
    private final CrackHashService crackHashService;

    private final WorkerResultProducer workerResultProducer;

    public WorkerTaskConsumer(CrackHashService crackHashService, WorkerResultProducer workerResultProducer) {
        this.crackHashService = crackHashService;
        this.workerResultProducer = workerResultProducer;
    }

    @RabbitListener(queues = "${crack-hash.rabbitmq.queue.worker}")
    public void consumeWorkerTask(WorkerTaskRequest workerTask) {
        log.info("Request #{}. Receiving part #{}", workerTask.getRequestId(), workerTask.getPartNumber());

        WorkerResultResponse workerResult = crackHashService.crackHash(workerTask);
        workerResultProducer.sendResult(workerResult);
    }
}
