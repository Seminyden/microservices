package com.gmail.seminyden.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageDTO {

    private Long id;
    @NotNull
    private String storageType;
    @NotBlank
    private String bucket;
    @NotBlank
    private String path;
}