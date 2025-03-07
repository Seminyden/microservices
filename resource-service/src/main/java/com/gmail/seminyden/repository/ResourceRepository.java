package com.gmail.seminyden.repository;

import com.gmail.seminyden.entity.ResourceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends CrudRepository<ResourceEntity, Integer> {
}