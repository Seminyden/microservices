package com.gmail.seminyden.listener;

import com.gmail.seminyden.service.SongMetadataService;
import com.gmail.seminyden.service.client.ResourceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ResourceProcessorQueueListener {

    private final ResourceClient resourceClient;
    private final SongMetadataService songMetadataService;

    @RabbitListener(queues = "${app.resource.processing.queue}")
    public void process(Integer resourceId) {
        log.info("ResourceId received: {}", resourceId);
        byte[] resource = resourceClient.getResourceById(resourceId);
        songMetadataService.createSongMetadata(resourceId, resource);
        log.info("Resource '{}' processed successfully", resourceId);
    }
}