package com.gmail.seminyden.contract;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import au.com.dius.pact.core.model.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PactConsumerTest
@PactDirectory("../pacts")
@PactTestFor(providerName = "resource-service", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
@ActiveProfiles("test")
public class RabbitMQContractTest {

    @BeforeEach
    public void setUp(List<Message> messages) {
        assertNotNull(messages);
    }

    @Pact(consumer = "resource-processor")
    public V4Pact createResourceRabbitMqContract(MessagePactBuilder builder) {
        return builder
                .given("Message in queue")
                .expectsToReceive("Message with resource id")
                .withContent("1")
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createResourceRabbitMqContract")
    void test(V4Interaction.AsynchronousMessage message) {
        String content = message.contentsAsString();
        assertEquals("1", content);
    }
}