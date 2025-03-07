package com.gmail.seminyden.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResourceS3Service {

    private final S3Client s3Client;

    public void put(String bucket, String key, byte[] resource) {
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                RequestBody.fromBytes(resource)
        );
    }

    public byte[] get(String bucket, String key) {
        try(ResponseInputStream<GetObjectResponse> response =
                    s3Client.getObject(
                            GetObjectRequest.builder()
                                    .bucket(bucket)
                                    .key(key)
                                    .build()
                    )) {
            return response.readAllBytes();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String bucket, String key) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build()
        );
    }
}