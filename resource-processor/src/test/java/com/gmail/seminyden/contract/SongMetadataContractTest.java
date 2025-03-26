package com.gmail.seminyden.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import com.google.gson.Gson;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@PactConsumerTest
@PactDirectory("../pacts")
@PactTestFor(providerName = "song-service")
@ActiveProfiles("test")
public class SongMetadataContractTest {

    private static final String SONG_ENDPOINT = "/songs";

    @BeforeEach
    public void setUp(MockServer mockServer) {
        assertNotNull(mockServer);
    }

    @Pact(consumer = "resource-processor")
    public V4Pact createSongMetadataContract(PactDslWithProvider builder) {
        return builder
                .given("Song metadata can be created")
                .uponReceiving("Request to create song metadata")
                .path(SONG_ENDPOINT)
                .method("POST")
                .body(getCreateSongMetadataRequestPayload())
                .willRespondWith()
                .status(200)
                .body(getCreateSongMetadataResponsePayload())
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createSongMetadataContract")
    public void testCreateSongMetadata(MockServer mockServer) throws IOException {
        Content content = Request.post(mockServer.getUrl() + SONG_ENDPOINT)
                .bodyString(getCreateSongMetadataRequestPayload(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent();
        assertEquals(getCreateSongMetadataResponsePayload(), content.asString());
    }

    private String getCreateSongMetadataRequestPayload() {
        return new Gson().toJson(
                SongMetadataDTO.builder()
                        .id("1")
                        .name("Test name")
                        .artist("Test artist")
                        .album("Test album")
                        .duration("01:05")
                        .year("1999")
                        .build()
        );
    }

    private String getCreateSongMetadataResponsePayload() {
        return new Gson().toJson(
                EntityIdDTO.builder()
                        .id(1)
                        .build()
        );
    }
}