package com.gmail.seminyden.listener;

import com.gmail.seminyden.model.StorageType;
import com.gmail.seminyden.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ResourceServiceQueueListener {

    private static final String TRACE_ID = "traceId";

    private final ResourceService resourceService;

    @RabbitListener(queues = "${app.resource.processed.queue}")
    public void process(Integer resourceId, @Header(name = "X-Trace-Id", required = false) String traceId) {
        setTraceId(traceId);
        log.info("ResourceId received: {}", resourceId);
        resourceService.moveResource(StorageType.STAGING, StorageType.PERMANENT, resourceId);
        log.info("Resource {} successfully moved from STAGING to PERMANENT storage", resourceId);
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