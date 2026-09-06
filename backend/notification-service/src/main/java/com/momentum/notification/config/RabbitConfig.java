package com.momentum.notification.config;

import com.momentum.common.event.EventTypes;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE = "momentum.notification.events";

    @Bean
    TopicExchange momentumExchange() {
        return new TopicExchange(EventTypes.EXCHANGE, true, false);
    }

    @Bean
    Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE).withArgument("x-dead-letter-exchange", EventTypes.DLX).build();
    }

    @Bean
    Binding binding(Queue notificationQueue, TopicExchange momentumExchange) {
        return BindingBuilder.bind(notificationQueue).to(momentumExchange).with("momentum.#");
    }

    @Bean
    JacksonJsonMessageConverter converter() {
        return new JacksonJsonMessageConverter();
    }
}
