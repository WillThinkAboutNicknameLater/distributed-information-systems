package ru.nsu.fit.crackhash.manager.queue.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.manager.config.RabbitProperties;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerTaskRequest;

@Slf4j
@Service
public class WorkerTaskProducer {
    private final RabbitProperties rabbitProperties;

    private final RabbitTemplate rabbitTemplate;

    public WorkerTaskProducer(RabbitProperties rabbitProperties, RabbitTemplate rabbitTemplate) {
        this.rabbitProperties = rabbitProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendTask(WorkerTaskRequest workerTask) {
        log.info("Request #{}. Sending part #{}", workerTask.getRequestId(), workerTask.getPartNumber());

        rabbitTemplate.convertAndSend(rabbitProperties.getDirectExchangeName(),
                                      rabbitProperties.getWorkerRoutingKey(),
                                      workerTask);
    }
}
