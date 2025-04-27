package com.gmail.seminyden.config;

import org.apache.tika.parser.AutoDetectParser;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${app.resource.processing.queue}")
    private String resourceProcessingQueueName;
    @Value("${app.resource.processed.queue}")
    private String resourceProcessedQueueName;

    @Bean
    public AutoDetectParser autoDetectParser() {
        return new AutoDetectParser();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Queue resourceProcessingQueue() {
        return new Queue(resourceProcessingQueueName);
    }

    @Bean
    public Queue resourceProcessedQueue() {
        return new Queue(resourceProcessedQueueName);
    }
}