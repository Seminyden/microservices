package com.gmail.seminyden.component;

import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.repository.ResourceRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class ResourceSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ResourceRepository resourceRepository;

    private byte[] resource;
    private ResponseEntity<?> response;
    private Integer createResourceId;

    @Before
    public void setup() {
    }

    @Given("the database is clean")
    public void theDatabaseIsClean() {
        resourceRepository.deleteAll();
    }

    @Given("I have a valid file to upload")
    public void iHaveAValidFileToUpload() {
        resource = getTestMp3File();
    }

    @When("I upload the file")
    public void iUploadTheFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(resource, headers);

        response = restTemplate.exchange(
                "/resources",
                HttpMethod.POST,
                requestEntity,
                EntityIdDTO.class
        );
    }

    @And("I should receive a success response")
    public void iShouldReceiveASuccessResponse() {
        assertTrue(response.getStatusCode().is2xxSuccessful());
        EntityIdDTO responseBody = (EntityIdDTO) response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getId());
        createResourceId = responseBody.getId();
    }

    private byte[] getTestMp3File() {
        try {
            Path path = Paths.get(getClass().getResource("/test-files/valid-sample-with-required-tags.mp3").toURI());
            return Files.readAllBytes(path);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to read test MP3 file", e);
        }
    }
} 