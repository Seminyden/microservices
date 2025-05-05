package com.gmail.seminyden.config;

import com.gmail.seminyden.filter.TraceIdFilter;
import com.gmail.seminyden.interceptor.TraceIdInterceptor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.Collections;

@Configuration
@EnableRabbit
public class AppConfig {

    @Value("${aws.access.key.id}")
    private String accessKeyId;
    @Value("${aws.secret.access.key}")
    private String secretAccessKey;
    @Value("${aws.s3.region}")
    private String s3Region;
    @Value( "${aws.url}")
    private String url;

    @Value("${app.resource.processing.queue}")
    private String resourceProcessingQueueName;
    @Value("${app.resource.processed.queue}")
    private String resourceProcessedQueueName;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .forcePathStyle(true)
                .region(Region.of(s3Region))
                .endpointOverride(URI.create(url))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        )
                )
                .build();
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