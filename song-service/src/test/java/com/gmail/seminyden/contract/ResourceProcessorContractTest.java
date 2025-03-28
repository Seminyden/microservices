package com.gmail.seminyden.contract;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import com.gmail.seminyden.controller.SongController;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.service.SongMetadataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;

@Provider("song-service")
@PactFolder("../pacts")
@IgnoreNoPactsToVerify(ignoreIoErrors = "true")
public class ResourceProcessorContractTest {

    @Mock
    private SongMetadataService songMetadataService;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        if (context != null) {
            MockitoAnnotations.openMocks(this);
            MockMvcTestTarget testTarget = new MockMvcTestTarget();
            testTarget.setControllers(new SongController(songMetadataService));
            context.setTarget(testTarget);
        }
    }

    @State("Song metadata can be created")
    public void createSongMetadata() {
        Mockito.when(songMetadataService.createSongMetadata(any()))
                .thenReturn(
                        EntityIdDTO.builder()
                                .id(1)
                                .build()
                );
    }
}