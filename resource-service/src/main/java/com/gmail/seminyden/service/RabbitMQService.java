package com.gmail.seminyden.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String queueName, Object message) {
        log.info("Send message {} to {}", message, queueName);
        rabbitTemplate.convertAndSend(queueName, message);
        log.info("Message sent successfully");
    }
}