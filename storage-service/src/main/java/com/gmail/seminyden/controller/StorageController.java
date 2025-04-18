package com.gmail.seminyden.controller;

import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.StorageDTO;
import com.gmail.seminyden.service.StorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<EntityIdDTO> createStorage(
            @Valid
            @NotNull(message = "Storage is missing")
            @RequestBody
            StorageDTO storage)
    {
        EntityIdDTO entityId = storageService.createStorage(storage);
        return ResponseEntity.ok(entityId);
    }

    @GetMapping
    public ResponseEntity<List<StorageDTO>> getAllStorages() {
        List<StorageDTO> storages = storageService.getAllStorages();
        return ResponseEntity.ok(storages);
    }

    @DeleteMapping
    public ResponseEntity<EntityIdsDTO> deleteStorages(
            @Valid
            @Size(max = 200, message = "Ids length must be less than 200 characters")
            @Pattern(regexp = "^[1-9]\\d*(?:,\\d+)*$", message = "Should be comma-separated list of IDs")
            @PathParam("id")
            String id
    ) {
        EntityIdsDTO entityIds = storageService.deleteStorages(id);
        return ResponseEntity.ok(entityIds);
    }
}