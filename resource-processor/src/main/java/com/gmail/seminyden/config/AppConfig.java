package com.gmail.seminyden.config;

import com.gmail.seminyden.filter.TraceIdFilter;
import com.gmail.seminyden.interceptor.TraceIdInterceptor;
import org.apache.tika.parser.AutoDetectParser;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

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
    public Queue resourceProcessingQueue() {
        return new Queue(resourceProcessingQueueName);
    }

    @Bean
    public Queue resourceProcessedQueue() {
        return new Queue(resourceProcessedQueueName);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new TraceIdInterceptor()));
        return restTemplate;
    }

    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilter() {
        FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
}