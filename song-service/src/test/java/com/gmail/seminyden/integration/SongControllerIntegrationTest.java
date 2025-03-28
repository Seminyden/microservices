package com.gmail.seminyden.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SongControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SongMetadataDTO testSongMetadata;

    @BeforeEach
    void setUp() throws Exception {
        testSongMetadata = new SongMetadataDTO();
        testSongMetadata.setId("1");
        testSongMetadata.setName("Test Song");
        testSongMetadata.setArtist("Test Artist");
        testSongMetadata.setAlbum("Test Album");
        testSongMetadata.setDuration("3:30");
        testSongMetadata.setYear("2024");
    }

    @Test
    void createSongMetadata_Success() throws Exception {
        testSongMetadata.setId("1");

        MvcResult result = mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSongMetadata)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn();

        EntityIdDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                EntityIdDTO.class
        );
        assertEquals(1, response.getId());
    }

    @Test
    void createSongMetadata_ReturnsBadRequest_WhenMetadataIsInvalid() throws Exception {
        testSongMetadata.setName(null);
        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSongMetadata)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSongMetadata_Success() throws Exception {
        testSongMetadata.setId("2");

        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSongMetadata)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/songs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Test Song"))
                .andReturn();

        SongMetadataDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SongMetadataDTO.class
        );
        assertEquals("2", response.getId());
        assertEquals("Test Song", response.getName());
    }

    @Test
    void getSongMetadata_ReturnsNotFound_WhenMetadataDoesNotExist() throws Exception {
        mockMvc.perform(get("/songs/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSongsMetadata_Success() throws Exception {
        testSongMetadata.setId("3");

        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSongMetadata)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(delete("/songs?id=3"))
                .andExpect(status().isOk())
                .andReturn();

        EntityIdsDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                EntityIdsDTO.class
        );
        assertEquals(1, response.getIds().size());
        assertTrue(response.getIds().contains("3"));
    }

    @Test
    void deleteSongsMetadata_ReturnsBadRequest_WhenIdsAreInvalid() throws Exception {
        mockMvc.perform(delete("/songs?id=invalid"))
                .andExpect(status().isBadRequest());
    }
} 