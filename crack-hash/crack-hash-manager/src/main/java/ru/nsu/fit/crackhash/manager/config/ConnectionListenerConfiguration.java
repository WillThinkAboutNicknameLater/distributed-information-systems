package ru.nsu.fit.crackhash.manager.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import ru.nsu.fit.crackhash.manager.service.DeferredTaskService;

@Configuration
public class ConnectionListenerConfiguration {
    public ConnectionListenerConfiguration(ConnectionFactory connectionFactory, DeferredTaskService deferredTaskService) {
        connectionFactory.addConnectionListener(connection -> deferredTaskService.distributeTasks());
    }
}
