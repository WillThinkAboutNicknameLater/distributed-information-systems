package ru.nsu.fit.crackhash.manager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class RabbitProperties {
    @Value("${crack-hash.rabbitmq.exchange.direct}")
    private String directExchangeName;

    @Value("${crack-hash.rabbitmq.route.manager}")
    private String managerRoutingKey;

    @Value("${crack-hash.rabbitmq.route.worker}")
    private String workerRoutingKey;

    @Value("${crack-hash.rabbitmq.queue.manager}")
    private String managerQueueName;

    @Value("${crack-hash.rabbitmq.queue.worker}")
    private String workerQueueName;
}
