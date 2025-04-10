package com.gmail.seminyden.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.resource.service.host}")
    private String resourceServiceUrl;
    @Value("${app.song.service.host}")
    private String songServiceUrl;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/resources/**") .uri(resourceServiceUrl + "/resources"))
                .route(r -> r.path("/songs/**")     .uri(songServiceUrl + "/songs"))
                .build();
    }

    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }
}