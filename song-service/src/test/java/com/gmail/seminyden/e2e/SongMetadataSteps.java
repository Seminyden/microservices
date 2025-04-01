package com.gmail.seminyden.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.seminyden.component.TestConfig;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SongMetadataSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private SongMetadataDTO songMetadata;
    private ResponseEntity<?> response;
    private Integer createdSongId;

    @Before
    public void setup() {
        songMetadata = new SongMetadataDTO();
        songMetadata.setName("Test Song");
        songMetadata.setArtist("Test Artist");
        songMetadata.setAlbum("Test Album");
        songMetadata.setDuration("4:00");
        songMetadata.setYear("2024");
    }

    @Given("a new song {word} with complete metadata")
    public void givenNewSongWithCompleteMetadata(String id) {
        songMetadata.setId(id);
        assertNotNull(songMetadata);
        assertEquals("Test Song", songMetadata.getName());
        assertEquals("Test Artist", songMetadata.getArtist());
        assertEquals("Test Album", songMetadata.getAlbum());
        assertEquals("4:00", songMetadata.getDuration());
        assertEquals("2024", songMetadata.getYear());
    }

    @When("the song {word} is created in the system")
    public void whenSongIsCreated(String id) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        songMetadata.setId(id);
        HttpEntity<String> request = new HttpEntity<>(
                objectMapper.writeValueAsString(songMetadata),
                headers
        );

        response = restTemplate.postForEntity(
                "/songs",
                request,
                EntityIdDTO.class
        );
    }

    @Then("the system should return a valid song ID = {int}")
    public void thenSystemReturnsValidSongId(Integer id) {
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertInstanceOf(EntityIdDTO.class, response.getBody());
        createdSongId = ((EntityIdDTO) response.getBody()).getId();
        assertNotNull(createdSongId);
        assertEquals(id, createdSongId);
    }

    @Given("a song {word} exists in the system")
    public void givenSongExists(String id) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        songMetadata.setId(id);
        HttpEntity<String> request = new HttpEntity<>(
                objectMapper.writeValueAsString(songMetadata),
                headers
        );

        ResponseEntity<EntityIdDTO> createResponse = restTemplate.postForEntity(
                "/songs",
                request,
                EntityIdDTO.class
        );
        createdSongId = createResponse.getBody().getId();
    }

    @When("the song {word} metadata is retrieved")
    public void whenSongMetadataRetrieved(String id) {
        response = restTemplate.getForEntity(
                "/songs/" + id,
                SongMetadataDTO.class
        );
    }

    @Then("the system should return the complete song {word} metadata")
    public void thenSystemReturnsCompleteMetadata(String id) {
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertInstanceOf(SongMetadataDTO.class, response.getBody());
        SongMetadataDTO retrievedMetadata = (SongMetadataDTO) response.getBody();
        assertEquals(songMetadata.getId(), id);
        assertEquals(songMetadata.getName(), retrievedMetadata.getName());
        assertEquals(songMetadata.getArtist(), retrievedMetadata.getArtist());
        assertEquals(songMetadata.getAlbum(), retrievedMetadata.getAlbum());
        assertEquals(songMetadata.getDuration(), retrievedMetadata.getDuration());
        assertEquals(songMetadata.getYear(), retrievedMetadata.getYear());
    }

    @When("the song {word} is deleted")
    public void whenSongIsDeleted(String id) {
        response = restTemplate.exchange(
                "/songs?id=" + id,
                HttpMethod.DELETE,
                null,
                EntityIdsDTO.class
        );
    }

    @Then("the system should confirm the deletion of {word} song metadata")
    public void thenSystemConfirmsDeletion(String id) {
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertInstanceOf(EntityIdsDTO.class, response.getBody());
        EntityIdsDTO deleteResponse = (EntityIdsDTO) response.getBody();
        assertTrue(deleteResponse.getIds().contains(id));
    }
} 