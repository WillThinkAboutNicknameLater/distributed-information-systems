package ru.nsu.fit.crackhash.manager.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import ru.nsu.fit.crackhash.manager.CrackHashManagerApplication;

@Configuration
public class RabbitTemplateConfiguration {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(marshallingMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MarshallingMessageConverter marshallingMessageConverter() {
        return new MarshallingMessageConverter(jaxb2Marshaller());
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan(CrackHashManagerApplication.class.getPackageName());
        return jaxb2Marshaller;
    }
}
