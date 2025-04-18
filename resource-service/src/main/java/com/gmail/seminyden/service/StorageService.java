package com.gmail.seminyden.service;

import com.gmail.seminyden.client.StorageClient;
import com.gmail.seminyden.exception.StorageNotFoundException;
import com.gmail.seminyden.model.StorageDTO;
import com.gmail.seminyden.model.StorageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class StorageService {

    private final ResourceS3Service resourceS3Service;
    private final StorageClient storageClient;

    public void saveResource(StorageType storageType, String resourceKey, byte[] resource) {
        StorageDTO storageData = getStorage(storageType);
        log.info("Save resource to {}/{}", storageData.getBucket(), storageData.getPath() + resourceKey);
        resourceS3Service.put(storageData.getBucket(), storageData.getPath() + resourceKey, resource);
    }

    public byte[] getResource(StorageType storageType, String resourceKey) {
        StorageDTO storageData = getStorage(storageType);
        log.info("Get resource {}/{}", storageData.getBucket(), storageData.getPath() + resourceKey);
        return resourceS3Service.get(storageData.getBucket(), storageData.getPath() + resourceKey);
    }

    public void deleteResource(StorageType storageType, String resourceKey) {
        StorageDTO storageData = getStorage(storageType);
        log.info("Delete resource {}/{}", storageData.getBucket(), storageData.getPath() + resourceKey);
        resourceS3Service.delete(storageData.getBucket(), storageData.getPath() + resourceKey);
    }

    private StorageDTO getStorage(StorageType storageType) {
        return storageClient.getAllStorages().stream()
                .filter(storage -> storageType.name().equalsIgnoreCase(storage.getStorageType()))
                .findFirst()
                .orElseThrow(() -> new StorageNotFoundException("Storage '" + storageType + "' not found"));
    }
}