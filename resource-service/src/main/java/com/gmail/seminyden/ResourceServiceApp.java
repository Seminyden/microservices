package com.gmail.seminyden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRetry
public class ResourceServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServiceApp.class, args);
    }
}