package com.gmail.seminyden.config;

import org.apache.tika.parser.AutoDetectParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AutoDetectParser autoDetectParser() {
        return new AutoDetectParser();
    }
}