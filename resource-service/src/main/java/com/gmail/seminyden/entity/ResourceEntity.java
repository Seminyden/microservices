package com.gmail.seminyden.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resources")
@Getter
@Setter
public class ResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "\"key\"")
    private String key;
    @Column(name = "storage_type")
    private String storageType;
}