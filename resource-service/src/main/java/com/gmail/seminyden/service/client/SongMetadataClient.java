package com.gmail.seminyden.service.client;

import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SongMetadataClient {

    @Value("${app.songs.metadata.base.url}")
    private String baseUrl;

    @Value("${app.songs.metadata.create.endpoint}")
    private String createEndpoint;

    @Value("${app.songs.metadata.delete.endpoint}")
    private String deleteEndpoint;

    @Resource
    private RestTemplate restTemplate;

    public EntityIdDTO create(SongMetadataDTO songMetadataDTO) {
        System.out.println(baseUrl + createEndpoint);
        return restTemplate.postForObject(
                baseUrl + createEndpoint,
                new HttpEntity<>(songMetadataDTO, getHeaders()),
                EntityIdDTO.class
        );
    }

    public EntityIdsDTO delete(String ids) {
        System.out.println(baseUrl + deleteEndpoint + ids);
        return restTemplate.exchange(
                baseUrl + deleteEndpoint + ids,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                EntityIdsDTO.class
        ).getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
}