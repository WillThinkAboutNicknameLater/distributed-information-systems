package ru.nsu.fit.crackhash.manager.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitExchangeConfiguration {
    private final RabbitProperties rabbitProperties;

    public RabbitExchangeConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitProperties.getDirectExchangeName());
    }
}
