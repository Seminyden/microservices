package com.gmail.seminyden.service;

import com.gmail.seminyden.entity.SongMetadataEntity;
import com.gmail.seminyden.excpetion.MetadataAlreadyExistsException;
import com.gmail.seminyden.excpetion.MetadataNotFoundException;
import com.gmail.seminyden.mapper.SongMetadataMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import com.gmail.seminyden.repository.SongMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongMetadataService {

    private final SongMetadataRepository songMetadataRepository;
    private final SongMetadataMapper songMetadataMapper;

    public EntityIdDTO createSongMetadata(SongMetadataDTO songMetadata) {
        if (!songMetadataRepository.existsById(songMetadata.getId())) {
            SongMetadataEntity savedEntity = songMetadataRepository.save(
                    songMetadataMapper.toSongMetadataEntity(songMetadata)
            );
            return songMetadataMapper.toEntityIdDTO(savedEntity);
        } else {
            throw new MetadataAlreadyExistsException("Metadata for this ID already exists.");
        }
    }

    public SongMetadataDTO getSongMetadata(String id) {
        return songMetadataRepository.findById(id)
                .map(songMetadataMapper::toSongMetadataDTO)
                .orElseThrow(() ->
                        new MetadataNotFoundException("Song metadata with the specified ID does not exist.")
                );
    }

    public EntityIdsDTO deleteSongsMetadata(String ids) {
        List<String> deletedSongMetadataIds = new ArrayList<>();
        songMetadataRepository.findAllById(songMetadataMapper.toIdList(ids))
                .forEach(songMetadataEntity -> {
                    deletedSongMetadataIds.add(songMetadataEntity.getId());
                    songMetadataRepository.delete(songMetadataEntity);
                });
        return songMetadataMapper.toEntityIdsDTO(deletedSongMetadataIds);
    }
}