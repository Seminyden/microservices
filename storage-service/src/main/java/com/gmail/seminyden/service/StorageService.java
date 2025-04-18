package com.gmail.seminyden.service;

import com.gmail.seminyden.entity.StorageEntity;
import com.gmail.seminyden.excpetion.StorageAlreadyExistsException;
import com.gmail.seminyden.mapper.StorageMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.StorageDTO;
import com.gmail.seminyden.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;

    public EntityIdDTO createStorage(StorageDTO storage) {
        if (storageRepository.existsByStorageType(storage.getStorageType())) {
            throw new StorageAlreadyExistsException("Storage with '" + storage.getStorageType() + "' already exists");
        }
        StorageEntity storageEntity = storageRepository.save(storageMapper.toStorageEntity(storage));
        return storageMapper.toEntityIdDTO(storageEntity);
    }

    public List<StorageDTO> getAllStorages() {
        return storageRepository.findAll().stream()
                .map(storageMapper::toStorageDTO)
                .toList();
    }

    public EntityIdsDTO deleteStorages(String ids) {
        List<Long> deletedStorageIds = new ArrayList<>();
        storageRepository.findAllById(storageMapper.toIdList(ids))
                .forEach(storageEntity -> {
                    deletedStorageIds.add(storageEntity.getId());
                    storageRepository.delete(storageEntity);
                });
        return storageMapper.toEntityIdsDTO(deletedStorageIds);
    }
}