package ru.nsu.fit.crackhash.worker.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitBindingConfiguration {
    private final RabbitProperties rabbitProperties;

    public RabbitBindingConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Declarables bindings(DirectExchange directExchange, Queue managerQueue, Queue workerQueue) {
        return new Declarables(BindingBuilder.bind(managerQueue).to(directExchange).with(rabbitProperties.getManagerRoutingKey()),
                               BindingBuilder.bind(workerQueue).to(directExchange).with(rabbitProperties.getWorkerRoutingKey()));
    }
}
