package com.gmail.seminyden.client;

import com.gmail.seminyden.model.StorageDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class StorageClient {

    @Value("${app.storage.service.base.url}")
    private String storageServiceHost;
    @Value("${app.storage.service.storages.endpoint}")
    private String storagesEndpoint;

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "storageCircuitBreaker", fallbackMethod = "getAllStoragesFallback")
    public List<StorageDTO> getAllStorages() {
        return restTemplate.exchange(
                storageServiceHost + storagesEndpoint,
                HttpMethod.GET,
                RequestEntity.EMPTY,
                new ParameterizedTypeReference<List<StorageDTO>>() {}
        ).getBody();
    }

    public List<StorageDTO> getAllStoragesFallback(Exception e) {
        log.warn("Can not extract storages from storage-service ({}) -> use fallback value", e.getMessage());
        return List.of(
                StorageDTO.builder()
                        .storageType("STAGING")
                        .bucket("ds-resources-us-east-1")
                        .path("staging/")
                        .build(),
                StorageDTO.builder()
                        .storageType("PERMANENT")
                        .bucket("ds-resources-us-east-1")
                        .path("permanent/")
                        .build()
        );
    }
}