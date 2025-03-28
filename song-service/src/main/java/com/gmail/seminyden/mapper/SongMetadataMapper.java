package com.gmail.seminyden.mapper;

import com.gmail.seminyden.entity.SongMetadataEntity;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SongMetadataMapper {

    public SongMetadataEntity toSongMetadataEntity(SongMetadataDTO dto) {
        SongMetadataEntity entity = new SongMetadataEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setArtist(dto.getArtist());
        entity.setAlbum(dto.getAlbum());
        entity.setDuration(dto.getDuration());
        entity.setYear(dto.getYear());
        return entity;
    }

    public SongMetadataDTO toSongMetadataDTO(SongMetadataEntity entity) {
        SongMetadataDTO dto = new SongMetadataDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setArtist(entity.getArtist());
        dto.setAlbum(entity.getAlbum());
        dto.setDuration(entity.getDuration());
        dto.setYear(entity.getYear());
        return dto;
    }

    public EntityIdDTO toEntityIdDTO(SongMetadataEntity entity) {
        return EntityIdDTO.builder()
                .id(toInt(entity.getId()))
                .build();
    }

    public EntityIdsDTO toEntityIdsDTO(List<String> ids) {
        return EntityIdsDTO.builder()
                .ids(ids)
                .build();
    }

    public List<String> toIdList(String id) {
        return Arrays.asList(id.split(","));
    }

    private Integer toInt(String value) {
        return Integer.parseInt(value);
    }
}