package com.gmail.seminyden.listener;

import com.gmail.seminyden.model.StorageType;
import com.gmail.seminyden.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ResourceServiceQueueListener {

    private final ResourceService resourceService;

    @RabbitListener(queues = "${app.resource.processed.queue}")
    public void process(Integer resourceId) {
        log.info("ResourceId received: {}", resourceId);
        resourceService.moveResource(StorageType.STAGING, StorageType.PERMANENT, resourceId);
        log.info("Resource {} successfully moved from STAGING to PERMANENT storage", resourceId);
    }
}