package com.gmail.seminyden.controller;

import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.service.ResourceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<?> createResource(
            @Valid
            @NotNull(message = "File should not be blank")
            @RequestBody
            byte[] resource
    ) {
        EntityIdDTO entityIdDTO = resourceService.createResource(resource);
        return ResponseEntity.ok(entityIdDTO);
    }

    @GetMapping(value = "/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getResource(
            @Valid
            @Pattern(regexp = "^[1-9]\\d*$", message = "The provided ID is invalid (e.g., contains letters, decimals, is negative, or zero)")
            @PathVariable("id")
            String id
    ) {
        byte[] resource = resourceService.getResource(id);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteResources(
            @Valid
            @Size(max = 200, message = "CSV string length must be less than 200 characters")
            @Pattern(regexp = "^\\d+(?:,\\d+)*$", message = "Should be comma-separated list of metadata IDs")
            @PathParam("id")
            String id
    ) {
        EntityIdsDTO entityIdsDTO = resourceService.deleteResources(id);
        return ResponseEntity.ok(entityIdsDTO);
    }
}