package com.momentum.streak.config;

import com.momentum.common.event.EventTypes;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE = "momentum.streak.journal";

    @Bean
    TopicExchange momentumExchange() {
        return new TopicExchange(EventTypes.EXCHANGE, true, false);
    }

    @Bean
    Queue streakJournalQueue() {
        return QueueBuilder.durable(QUEUE).withArgument("x-dead-letter-exchange", EventTypes.DLX).build();
    }

    @Bean
    Binding submittedBinding(Queue streakJournalQueue, TopicExchange momentumExchange) {
        return BindingBuilder.bind(streakJournalQueue).to(momentumExchange).with(EventTypes.JOURNAL_SUBMITTED);
    }

    @Bean
    Binding missedBinding(Queue streakJournalQueue, TopicExchange momentumExchange) {
        return BindingBuilder.bind(streakJournalQueue).to(momentumExchange).with(EventTypes.JOURNAL_MISSED);
    }

    @Bean
    JacksonJsonMessageConverter converter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, JacksonJsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }
}
