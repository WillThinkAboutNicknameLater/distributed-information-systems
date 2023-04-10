package ru.nsu.fit.crackhash.worker.queue.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.worker.config.RabbitProperties;
import ru.nsu.fit.crackhash.worker.queue.message.WorkerResultResponse;

@Slf4j
@Service
public class WorkerResultProducer {
    private final RabbitProperties rabbitProperties;

    private final RabbitTemplate rabbitTemplate;

    public WorkerResultProducer(RabbitProperties rabbitProperties, RabbitTemplate rabbitTemplate) {
        this.rabbitProperties = rabbitProperties;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendResult(WorkerResultResponse workerResult) {
        log.info("Request #{}. Sending completed part #{}", workerResult.getRequestId(), workerResult.getPartNumber());

        rabbitTemplate.convertAndSend(rabbitProperties.getDirectExchangeName(),
                                      rabbitProperties.getManagerRoutingKey(),
                                      workerResult);
    }
}
