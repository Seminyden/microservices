package com.gmail.seminyden.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private static final String TRACE_ID_ATTR   = "X-Trace-Id";
    private static final String TRACE_ID        = "traceId";

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String queueName, Object message) {
        log.info("Send message {} to {}", message, queueName);
        rabbitTemplate.convertAndSend(queueName, message, getMessagePostProcessor());
        log.info("Message sent successfully");
    }

    private MessagePostProcessor getMessagePostProcessor() {
        return msg -> {
            String traceId = MDC.get(TRACE_ID);
            log.debug("Add traceId {} to message", traceId);
            if (StringUtils.isNotBlank(traceId)) {
                msg.getMessageProperties().setHeader(TRACE_ID_ATTR, traceId);
            }
            return msg;
        };
    }
}