package com.gmail.seminyden.listener;

import com.gmail.seminyden.service.RabbitMQService;
import com.gmail.seminyden.service.SongMetadataService;
import com.gmail.seminyden.service.client.ResourceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ResourceProcessorQueueListener {

    private static final String TRACE_ID = "traceId";

    private final ResourceClient resourceClient;
    private final SongMetadataService songMetadataService;
    private final RabbitMQService rabbitMQService;

    @Value("${app.resource.processed.queue}")
    private String resourceProcessedQueueName;

    @RabbitListener(queues = "${app.resource.processing.queue}")
    public void process(Integer resourceId, @Header(name = "X-Trace-Id", required = false) String traceId) {
        setTraceId(traceId);
        log.info("ResourceId received: {}", resourceId);
        byte[] resource = resourceClient.getResourceById(resourceId);
        songMetadataService.createSongMetadata(resourceId, resource);
        log.info("Resource '{}' processed successfully", resourceId);
        rabbitMQService.sendMessage(resourceProcessedQueueName, resourceId);
        emptyTraceId();
    }

    private void setTraceId(String traceId) {
        log.debug("Add traceId {} from message", traceId);
        if (StringUtils.isNotBlank(traceId)) {
            MDC.put(TRACE_ID, traceId);
        }
    }

    private void emptyTraceId() {
        MDC.put(TRACE_ID, null);
    }
}