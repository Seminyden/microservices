package com.gmail.seminyden.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ResourceClient {

    private final RestTemplate restTemplate;

    @Value("${app.resource.service.host}")
    private String resourceServiceHost;
    @Value("${app.resource.service.resources.endpoint}")
    private String resourceEndpoint;

    @Retryable(backoff = @Backoff(delay = 1000, maxDelay = 16000, multiplier = 2), maxAttempts = 5)
    public byte[] getResourceById(Integer id) {
        return restTemplate.getForObject(resourceServiceHost + resourceEndpoint + id, byte[].class);
    }
}