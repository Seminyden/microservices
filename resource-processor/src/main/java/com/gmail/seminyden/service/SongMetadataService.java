package com.gmail.seminyden.service;

import com.gmail.seminyden.mapper.SongMetadataMapper;
import com.gmail.seminyden.model.SongMetadataDTO;
import lombok.RequiredArgsConstructor;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongMetadataService {

    private final MetadataService metadataService;
    private final SongMetadataMapper songMetadataMapper;

    public SongMetadataDTO getSongMetadata(String resourceId, byte[] resource) {
        Metadata metadata = metadataService.getMetadata(resource);
        return songMetadataMapper.toSongMetadataDTO(resourceId, metadata);
    }
}