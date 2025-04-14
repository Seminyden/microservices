package com.gmail.seminyden.unit;

import com.gmail.seminyden.entity.ResourceEntity;
import com.gmail.seminyden.mapper.ResourceMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.service.RabbitMQService;
import com.gmail.seminyden.service.ResourceService;
import com.gmail.seminyden.repository.ResourceRepository;
import com.gmail.seminyden.service.StorageService;
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
    private RabbitMQService rabbitMQService;
    @Mock
    private StorageService storageService;

    private ResourceService resourceService;
    private ResourceEntity testResource;

    @BeforeEach
    void setUp() {
        resourceService = new ResourceService(
                resourceRepository,
                rabbitMQService,
                new ResourceMapper(),
                storageService
        );

        testResource = new ResourceEntity();
        testResource.setId(1);
        testResource.setKey("test-key");
        testResource.setStorageType("STAGING");
    }

    @Test
    void createResource_Success() {
        when(resourceRepository.save(any(ResourceEntity.class))).thenReturn(testResource);

        EntityIdDTO result = resourceService.createResource(new byte[]{});

        assertNotNull(result);
        assertEquals(testResource.getId(), result.getId());
        verify(storageService).saveResource(any(), any(), any(byte[].class));
        verify(rabbitMQService).sendMessage(any(), any());
        verify(resourceRepository).save(any(ResourceEntity.class));
    }

    @Test
    void getResource_WhenResourceExists_Success() {
        when(resourceRepository.findById(1)).thenReturn(Optional.of(testResource));
        when(storageService.getResource(any(), any())).thenReturn(new byte[]{});

        byte[] result = resourceService.getResource("1");

        assertNotNull(result);
        verify(resourceRepository).findById(1);
        verify(storageService).getResource(any(), any());
    }

    @Test
    void deleteResource_WhenResourceExists_Success() {
        when(resourceRepository.findAllById(List.of(1, 2, 3))).thenReturn(List.of(testResource));

        resourceService.deleteResources("1,2,3");

        verify(resourceRepository).delete(testResource);
        verifyNoMoreInteractions(resourceRepository);
        verify(storageService).deleteResource(any(), any());
        verifyNoMoreInteractions(storageService);
    }
} 