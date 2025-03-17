package com.gmail.seminyden.service;

import com.gmail.seminyden.mapper.SongMetadataMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.service.client.SongMetadataClient;
import lombok.RequiredArgsConstructor;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongMetadataService {

    private final SongMetadataClient songMetadataClient;
    private final MetadataService metadataService;
    private final SongMetadataMapper songMetadataMapper;

    public EntityIdDTO createSongMetadata(Integer resourceId, byte[] resource) {
        Metadata songMetadata = metadataService.getMetadata(resource);
        return songMetadataClient.createSongMetadata(
                songMetadataMapper.toSongMetadataDTO(resourceId, songMetadata)
        );
    }
}