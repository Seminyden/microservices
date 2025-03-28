package com.gmail.seminyden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "song_metadata")
public class SongMetadataEntity {

    @Id
    private String id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    @Column(name = "\"year\"")
    private String year;
}