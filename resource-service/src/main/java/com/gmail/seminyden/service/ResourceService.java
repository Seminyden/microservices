package com.gmail.seminyden.service;

import com.gmail.seminyden.entity.ResourceEntity;
import com.gmail.seminyden.exception.ResourceNotFoundException;
import com.gmail.seminyden.mapper.ResourceMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.StorageType;
import com.gmail.seminyden.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final RabbitMQService rabbitMQService;
    private final ResourceMapper resourceMapper;
    private final StorageService storageService;

    @Value("${app.resource.processing.queue}")
    private String resourceProcessingQueueName;

    public EntityIdDTO createResource(byte[] resource) {
        String resourceKey = UUID.randomUUID().toString();
        log.info("Create resource with key '{}'", resourceKey);
        storageService.saveResource(StorageType.STAGING, resourceKey, resource);
        ResourceEntity resourceEntity = resourceRepository.save(
                resourceMapper.toResourceEntity(resourceKey, StorageType.STAGING)
        );
        rabbitMQService.sendMessage(resourceProcessingQueueName, resourceEntity.getId());
        log.info("Resource created '{}' - '{}' ({})",
                resourceEntity.getId(), resourceEntity.getKey(), resourceEntity.getStorageType());
        return resourceMapper.toEntityIdDTO(resourceEntity);
    }

    public byte[] getResource(String id) {
        log.info("Get resource by id '{}'", id);
        return resourceRepository.findById(resourceMapper.toInt(id))
                .map(resource ->
                        storageService.getResource(
                                StorageType.valueOf(resource.getStorageType()),
                                resource.getKey()
                        )
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resource with the specified ID does not exist.")
                );
    }

    public EntityIdsDTO deleteResources(String ids) {
        List<Integer> deletedResourceIds = new ArrayList<>();
        resourceRepository.findAllById(resourceMapper.toIntList(ids))
                .forEach(resourceEntity -> {
                    log.info("Delete resource '{}' - '{}' ({})",
                            resourceEntity.getId(), resourceEntity.getKey(), resourceEntity.getStorageType());
                    storageService.deleteResource(
                            StorageType.valueOf(resourceEntity.getStorageType()),
                            resourceEntity.getKey()
                    );
                    resourceRepository.delete(resourceEntity);
                    deletedResourceIds.add(resourceEntity.getId());
                });
        return resourceMapper.toEntityIdsDTO(deletedResourceIds);
    }

    public void moveResource(StorageType fromStorageType, StorageType toStorageType, Integer resourceId) {
        resourceRepository.findById(resourceId)
                .ifPresent(resourceEntity -> {
                    String resourceKey = resourceEntity.getKey();
                    byte[] resource = storageService.getResource(fromStorageType, resourceKey);
                    storageService.saveResource(toStorageType, resourceKey, resource);
                    storageService.deleteResource(fromStorageType, resourceKey);
                    resourceEntity.setStorageType(toStorageType.name());
                    resourceRepository.save(resourceEntity);
                });
    }
}