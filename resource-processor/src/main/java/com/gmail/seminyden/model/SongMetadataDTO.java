package com.gmail.seminyden.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SongMetadataDTO {

    private String id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}