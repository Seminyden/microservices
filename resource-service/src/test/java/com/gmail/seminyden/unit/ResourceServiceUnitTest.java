package com.gmail.seminyden.unit;

import com.gmail.seminyden.entity.ResourceEntity;
import com.gmail.seminyden.mapper.ResourceMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.service.RabbitMQService;
import com.gmail.seminyden.service.ResourceS3Service;
import com.gmail.seminyden.service.ResourceService;
import com.gmail.seminyden.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceUnitTest {

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private ResourceS3Service resourceS3Service;
    @Mock
    private RabbitMQService rabbitMQService;

    private ResourceService resourceService;
    private ResourceEntity testResource;

    @BeforeEach
    void setUp() {
        resourceService = new ResourceService(
                resourceRepository,
                resourceS3Service,
                rabbitMQService,
                new ResourceMapper()
        );

        testResource = new ResourceEntity();
        testResource.setId(1);
        testResource.setS3Bucket("test-bucket");
        testResource.setKey("test-key");
    }

    @Test
    void createResource_Success() {
        when(resourceRepository.save(any(ResourceEntity.class))).thenReturn(testResource);

        EntityIdDTO result = resourceService.createResource(new byte[]{});

        assertNotNull(result);
        assertEquals(testResource.getId(), result.getId());
        verify(resourceS3Service).put(any(), any(), any(byte[].class));
        verify(rabbitMQService).sendMessage(any(), any());
        verify(resourceRepository).save(any(ResourceEntity.class));
    }

    @Test
    void getResource_WhenResourceExists_Success() {
        when(resourceRepository.findById(1)).thenReturn(Optional.of(testResource));
        when(resourceS3Service.get(any(), any())).thenReturn(new byte[]{});

        byte[] result = resourceService.getResource("1");

        assertNotNull(result);
        verify(resourceRepository).findById(1);
        verify(resourceS3Service).get(any(), any());
    }

    @Test
    void deleteResource_WhenResourceExists_Success() {
        when(resourceRepository.findAllById(List.of(1, 2, 3))).thenReturn(List.of(testResource));

        resourceService.deleteResources("1,2,3");

        verify(resourceRepository).delete(testResource);
        verifyNoMoreInteractions(resourceRepository);
        verify(resourceS3Service).delete(anyString(), anyString());
        verifyNoMoreInteractions(resourceS3Service);
    }
} 