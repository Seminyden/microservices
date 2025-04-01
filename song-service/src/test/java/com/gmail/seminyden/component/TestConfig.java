package com.gmail.seminyden.component;

import com.gmail.seminyden.entity.SongMetadataEntity;
import com.gmail.seminyden.mapper.SongMetadataMapper;
import com.gmail.seminyden.repository.SongMetadataRepository;
import com.gmail.seminyden.service.SongMetadataService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public SongMetadataService songMetadataService() {
        return new SongMetadataService(new TestSongMetadataRepository(), new SongMetadataMapper());
    }

    static class TestSongMetadataRepository implements SongMetadataRepository {

        private Map<String, SongMetadataEntity> songMetadataMap = new HashMap<>();

        @Override
        public <S extends SongMetadataEntity> S save(S entity) {
            songMetadataMap.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public <S extends SongMetadataEntity> Iterable<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<SongMetadataEntity> findById(String s) {
            return Optional.ofNullable(songMetadataMap.get(s));
        }

        @Override
        public boolean existsById(String s) {
            return false;
        }

        @Override
        public Iterable<SongMetadataEntity> findAll() {
            return null;
        }

        @Override
        public Iterable<SongMetadataEntity> findAllById(Iterable<String> strings) {
            return StreamSupport.stream(strings.spliterator(), false)
                    .map(songMetadataMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(String s) {
            songMetadataMap.remove(s);
        }

        @Override
        public void delete(SongMetadataEntity entity) {
            songMetadataMap.remove(entity.getId());
        }

        @Override
        public void deleteAllById(Iterable<? extends String> strings) {

        }

        @Override
        public void deleteAll(Iterable<? extends SongMetadataEntity> entities) {

        }

        @Override
        public void deleteAll() {

        }
    }
}