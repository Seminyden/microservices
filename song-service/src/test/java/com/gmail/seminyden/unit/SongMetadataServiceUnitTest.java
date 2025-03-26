package com.gmail.seminyden.unit;

import com.gmail.seminyden.entity.SongMetadataEntity;
import com.gmail.seminyden.excpetion.MetadataAlreadyExistsException;
import com.gmail.seminyden.excpetion.MetadataNotFoundException;
import com.gmail.seminyden.mapper.SongMetadataMapper;
import com.gmail.seminyden.model.EntityIdDTO;
import com.gmail.seminyden.model.EntityIdsDTO;
import com.gmail.seminyden.model.SongMetadataDTO;
import com.gmail.seminyden.repository.SongMetadataRepository;
import com.gmail.seminyden.service.SongMetadataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongMetadataServiceUnitTest {

    @Mock
    private SongMetadataRepository songMetadataRepository;

    @Mock
    private SongMetadataMapper songMetadataMapper;

    @InjectMocks
    private SongMetadataService songMetadataService;

    private SongMetadataDTO testSongMetadata;
    private SongMetadataEntity testSongMetadataEntity;
    private EntityIdDTO testEntityIdDTO;

    @BeforeEach
    void setUp() {
        testSongMetadata = new SongMetadataDTO();
        testSongMetadata.setId("1");
        testSongMetadata.setName("Test Song");
        testSongMetadata.setArtist("Test Artist");
        testSongMetadata.setAlbum("Test Album");
        testSongMetadata.setDuration("3:30");
        testSongMetadata.setYear("2024");

        testSongMetadataEntity = new SongMetadataEntity();
        testSongMetadataEntity.setId("1");
        testSongMetadataEntity.setName("Test Song");
        testSongMetadataEntity.setArtist("Test Artist");
        testSongMetadataEntity.setAlbum("Test Album");
        testSongMetadataEntity.setDuration("3:30");
        testSongMetadataEntity.setYear("2024");

        testEntityIdDTO = EntityIdDTO.builder()
                .id(1)
                .build();
    }

    @Test
    void createSongMetadata_Success() {
        when(songMetadataRepository.existsById("1")).thenReturn(false);
        when(songMetadataMapper.toSongMetadataEntity(testSongMetadata)).thenReturn(testSongMetadataEntity);
        when(songMetadataRepository.save(testSongMetadataEntity)).thenReturn(testSongMetadataEntity);
        when(songMetadataMapper.toEntityIdDTO(testSongMetadataEntity)).thenReturn(testEntityIdDTO);

        EntityIdDTO result = songMetadataService.createSongMetadata(testSongMetadata);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(songMetadataRepository).existsById("1");
        verify(songMetadataRepository).save(testSongMetadataEntity);
    }

    @Test
    void createSongMetadata_ThrowsException_WhenMetadataExists() {
        when(songMetadataRepository.existsById("1")).thenReturn(true);

        assertThrows(MetadataAlreadyExistsException.class, () -> 
            songMetadataService.createSongMetadata(testSongMetadata)
        );
        verify(songMetadataRepository).existsById("1");
        verify(songMetadataRepository, never()).save(any());
    }

    @Test
    void getSongMetadata_Success() {
        when(songMetadataRepository.findById("1")).thenReturn(Optional.of(testSongMetadataEntity));
        when(songMetadataMapper.toSongMetadataDTO(testSongMetadataEntity)).thenReturn(testSongMetadata);

        SongMetadataDTO result = songMetadataService.getSongMetadata("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Test Song", result.getName());
        verify(songMetadataRepository).findById("1");
    }

    @Test
    void getSongMetadata_ThrowsException_WhenMetadataNotFound() {
        when(songMetadataRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(MetadataNotFoundException.class, () -> 
            songMetadataService.getSongMetadata("1")
        );
        verify(songMetadataRepository).findById("1");
        verify(songMetadataMapper, never()).toSongMetadataDTO(any());
    }

    @Test
    void deleteSongsMetadata_Success() {
        String ids = "1,2,3";
        SongMetadataEntity entity2 = new SongMetadataEntity();
        entity2.setId("2");
        SongMetadataEntity entity3 = new SongMetadataEntity();
        entity3.setId("3");

        when(songMetadataMapper.toIdList(ids)).thenReturn(Arrays.asList("1", "2", "3"));
        when(songMetadataMapper.toEntityIdsDTO(Arrays.asList("1", "2", "3"))).thenReturn(
                EntityIdsDTO.builder()
                        .ids(Arrays.asList("1", "2", "3"))
                        .build()
        );
        when(songMetadataRepository.findAllById(Arrays.asList("1", "2", "3")))
            .thenReturn(Arrays.asList(testSongMetadataEntity, entity2, entity3));

        EntityIdsDTO result = songMetadataService.deleteSongsMetadata(ids);

        assertNotNull(result);
        assertEquals(3, result.getIds().size());
        verify(songMetadataRepository, times(3)).delete(any());
    }
} 