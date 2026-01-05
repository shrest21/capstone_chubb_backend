package com.inventoryapp.emailservicecapstone.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String EMAIL_QUEUE = "order.email.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public Binding orderEmailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(orderExchange())
                .with(ORDER_CREATED_ROUTING_KEY);
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
