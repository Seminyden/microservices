package com.gmail.seminyden.repository;

import com.gmail.seminyden.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<StorageEntity, Long> {

    boolean existsByStorageType(String storageType);
}