package com.gmail.seminyden.service.client;

import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Component
@RequiredArgsConstructor
public class SongMetadataClient {

    private final RestTemplate restTemplate;

    @Value("${app.song.service.host}")
    private String songServiceHost;
    @Value("${app.song.service.create.endpoint}")
    private String createSongMetadataEndpoint;

    @Retryable(backoff = @Backoff(delay = 1000, maxDelay = 16000, multiplier = 2), maxAttempts = 5)
    public EntityIdDTO createSongMetadata(SongMetadataDTO songMetadata) {
        log.info("Send create song metadata '{}' request for", songMetadata.getId());
        return restTemplate.postForObject(
                songServiceHost + createSongMetadataEndpoint,
                songMetadata,
                EntityIdDTO.class
        );
    }
}