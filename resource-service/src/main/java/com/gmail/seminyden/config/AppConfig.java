package com.gmail.seminyden.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableRabbit
public class AppConfig {

    @Value("${aws.access.key.id}")
    private String accessKeyId;
    @Value("${aws.secret.access.key}")
    private String secretAccessKey;
    @Value("${aws.s3.region}")
    private String s3Region;

    @Value("${app.resource.processing.queue}")
    private String resourceProcessingQueueName;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(s3Region))
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
}