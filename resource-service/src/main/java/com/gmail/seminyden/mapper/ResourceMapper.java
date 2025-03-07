package com.gmail.seminyden.mapper;

import com.gmail.seminyden.entity.ResourceEntity;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResourceMapper {

    private static final String COMMA = ",";

    public ResourceEntity toResourceEntity(String s3Bucket, String key) {
        ResourceEntity entity = new ResourceEntity();
        entity.setS3Bucket(s3Bucket);
        entity.setKey(key);
        return entity;
    }

    public EntityIdDTO toEntityIdDTO(ResourceEntity entity) {
        return EntityIdDTO.builder()
                .id(entity.getId())
                .build();
    }

    public EntityIdsDTO toEntityIdsDTO(List<Integer> ids) {
        return EntityIdsDTO.builder()
                .ids(ids)
                .build();
    }

    public List<Integer> toIntList(String ids) {
        return Arrays.stream(ids.split(COMMA))
                .map(this::toInt)
                .collect(Collectors.toList());
    }

    public Integer toInt(String value) {
        return Integer.valueOf(value);
    }
}