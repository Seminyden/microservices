package com.gmail.seminyden.service;

import com.gmail.seminyden.mapper.SongMetadataMapper;
import com.gmail.seminyden.model.SongMetadataDTO;
import jakarta.annotation.Resource;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Service;

@Service
public class SongMetadataService {

    @Resource
    private MetadataService metadataService;
    @Resource
    private SongMetadataMapper songMetadataMapper;

    public SongMetadataDTO getSongMetadata(String resourceId, byte[] resource) {
        Metadata metadata = metadataService.getMetadata(resource);
        return songMetadataMapper.toSongMetadataDTO(resourceId, metadata);
    }
}