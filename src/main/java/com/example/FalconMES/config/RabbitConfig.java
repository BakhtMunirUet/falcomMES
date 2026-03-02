package com.example.FalconMES.config;



import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


@Configuration
public class RabbitConfig {

    @Bean
    public Queue demoQueue() {
        return new Queue("demoQueue", true);
    }

    @Bean
    public TopicExchange demoExchange() {
        return new TopicExchange("demoExchange");
    }

    @Bean
    public Binding binding(Queue demoQueue, TopicExchange demoExchange) {
        return BindingBuilder.bind(demoQueue).to(demoExchange).with("demoRoutingKey");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate template(org.springframework.amqp.rabbit.connection.ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(jsonConverter());
        return template;
    }
}
