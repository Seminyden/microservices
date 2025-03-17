package com.gmail.seminyden.service;

import com.gmail.seminyden.entity.ResourceEntity;
import com.gmail.seminyden.exception.ResourceNotFoundException;
import com.gmail.seminyden.mapper.ResourceMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceS3Service resourceS3Service;
    private final RabbitMQService rabbitMQService;
    private final ResourceMapper resourceMapper;

    @Value("${aws.s3.resource.bucket}")
    private String resourceS3Bucket;
    @Value("${app.resource.processing.queue}")
    private String resourceProcessingQueueName;

    public EntityIdDTO createResource(byte[] resource) {
        String resourceKey = UUID.randomUUID().toString();
        resourceS3Service.put(resourceS3Bucket, resourceKey, resource);
        ResourceEntity resourceEntity = resourceRepository.save(
                resourceMapper.toResourceEntity(resourceS3Bucket, resourceKey)
        );
        rabbitMQService.sendMessage(resourceProcessingQueueName, resourceEntity.getId());
        return resourceMapper.toEntityIdDTO(resourceEntity);
    }

    public byte[] getResource(String id) {
        return resourceRepository.findById(resourceMapper.toInt(id))
                .map(resource -> resourceS3Service.get(resource.getS3Bucket(), resource.getKey()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resource with the specified ID does not exist.")
                );
    }

    public EntityIdsDTO deleteResources(String ids) {
        List<Integer> deletedResourceIds = new ArrayList<>();
        resourceRepository.findAllById(resourceMapper.toIntList(ids))
                .forEach(resourceEntity -> {
                    resourceS3Service.delete(
                            resourceEntity.getS3Bucket(),
                            resourceEntity.getKey()
                    );
                    resourceRepository.delete(resourceEntity);
                    deletedResourceIds.add(resourceEntity.getId());
                });
        return resourceMapper.toEntityIdsDTO(deletedResourceIds);
    }
}