package com.gmail.seminyden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StorageServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApp.class, args);
    }
}