package com.gmail.seminyden.controller;

import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import com.gmail.seminyden.service.SongMetadataService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongMetadataService songMetadataService;

    @PostMapping
    public ResponseEntity<EntityIdDTO> createSongMetadata(
            @Valid
            @NotNull(message = "Song metadata is missing")
            @RequestBody
            SongMetadataDTO songMetadata
    ) {
        EntityIdDTO entityIdDTO = songMetadataService.createSongMetadata(songMetadata);
        return ResponseEntity.ok(entityIdDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongMetadataDTO> getSongMetadata(
            @Valid
            @Pattern(regexp = "^[1-9]\\d*$", message = "Id should be numeric string")
            @PathVariable("id")
            String id
    ) {
        SongMetadataDTO songMetadataDTO = songMetadataService.getSongMetadata(id);
        return ResponseEntity.ok(songMetadataDTO);
    }

    @DeleteMapping
    public ResponseEntity<EntityIdsDTO> deleteSongsMetadata(
            @Valid
            @Size(max = 200, message = "Ids length must be less than 200 characters")
            @Pattern(regexp = "^[1-9]\\d*(?:,\\d+)*$", message = "Should be comma-separated list of metadata IDs")
            @PathParam("id")
            String id
    ) {
        EntityIdsDTO entityIdsDTO = songMetadataService.deleteSongsMetadata(id);
        return ResponseEntity.ok(entityIdsDTO);
    }
}