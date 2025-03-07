package com.gmail.seminyden.service;

import jakarta.annotation.Resource;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class MetadataService {

    @Resource
    private AutoDetectParser autoDetectParser;

    public Metadata getMetadata(byte[] resource) {
        try(InputStream inputStream = new ByteArrayInputStream(resource)) {

            Metadata metadata = new Metadata();
            autoDetectParser.parse(
                    inputStream,
                    new BodyContentHandler(),
                    metadata,
                    new ParseContext()
            );
            return metadata;

        } catch (IOException | TikaException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}