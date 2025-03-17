package com.gmail.seminyden.mapper;


import com.gmail.seminyden.model.SongMetadataDTO;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Component;

@Component
public class SongMetadataMapper {

    private static final String TITLE           = "dc:title";
    private static final String ARTIST          = "xmpDM:artist";
    private static final String ALBUM           = "xmpDM:album";
    private static final String DURATION        = "xmpDM:duration";
    private static final String RELEASE_DATE    = "xmpDM:releaseDate";

    public SongMetadataDTO toSongMetadataDTO(Integer id, Metadata metadata) {
        return SongMetadataDTO.builder()
                .id(String.valueOf(id))
                .name(metadata.get(TITLE))
                .artist(metadata.get(ARTIST))
                .album(metadata.get(ALBUM))
                .duration(convertTime(metadata.get(DURATION)))
                .year(metadata.get(RELEASE_DATE))
                .build();
    }

    private String convertTime(String totalSeconds) {
        int minutes = (int) (Double.parseDouble(totalSeconds) / 60);
        int seconds = (int) (Double.parseDouble(totalSeconds) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}