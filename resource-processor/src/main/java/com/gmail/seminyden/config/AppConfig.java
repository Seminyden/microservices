package com.gmail.seminyden.config;

import org.apache.tika.parser.AutoDetectParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public AutoDetectParser autoDetectParser() {
        return new AutoDetectParser();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}