package ru.nsu.fit.crackhash.manager.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConfiguration {
    private final RabbitProperties rabbitProperties;

    public RabbitQueueConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Queue managerQueue() {
        return new Queue(rabbitProperties.getManagerQueueName());
    }

    @Bean
    public Queue workerQueue() {
        return new Queue(rabbitProperties.getWorkerQueueName());
    }
}
