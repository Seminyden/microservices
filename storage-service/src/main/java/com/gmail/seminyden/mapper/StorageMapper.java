package com.gmail.seminyden.mapper;

import com.gmail.seminyden.entity.StorageEntity;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.StorageDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StorageMapper {

    public StorageEntity toStorageEntity(StorageDTO storageDTO) {
        StorageEntity entity = new StorageEntity();
        entity.setId(storageDTO.getId());
        entity.setStorageType(storageDTO.getStorageType());
        entity.setBucket(storageDTO.getBucket());
        entity.setPath(storageDTO.getPath());
        return entity;
    }

    public StorageDTO toStorageDTO(StorageEntity entity) {
        StorageDTO dto = new StorageDTO();
        dto.setId(entity.getId());
        dto.setStorageType(entity.getStorageType());
        dto.setBucket(entity.getBucket());
        dto.setPath(entity.getPath());
        return dto;
    }

    public List<Long> toIdList(String id) {
        return Arrays.stream(id.split(","))
                .map(Long::parseLong)
                .toList();
    }

    public EntityIdDTO toEntityIdDTO(StorageEntity entity) {
        return EntityIdDTO.builder()
                .id(entity.getId())
                .build();
    }

    public EntityIdsDTO toEntityIdsDTO(List<Long> ids) {
        return EntityIdsDTO.builder()
                .ids(ids)
                .build();
    }
}