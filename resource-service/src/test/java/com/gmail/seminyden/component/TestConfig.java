package com.gmail.seminyden.component;

import com.gmail.seminyden.service.StorageService;
import lombok.SneakyThrows;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.ResponseInputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestConfig {

    private final Map<String, byte[]> mockS3Storage = new HashMap<>();

    @Bean
    @Primary
    public S3Client s3Client() {
        return new S3Client() {

            @Override
            public void close() {
            }

            @Override
            public String serviceName() {
                return "S3_MOCK_CLIENT";
            }

            @SneakyThrows
            @Override
            public PutObjectResponse putObject(PutObjectRequest request, RequestBody requestBody) {
                try(InputStream inputStream = requestBody.contentStreamProvider().newStream()) {
                    mockS3Storage.put(request.bucket() + request.key(), inputStream.readAllBytes());
                }
                return PutObjectResponse.builder().build();
            }

            @Override
            public ResponseInputStream<GetObjectResponse> getObject(GetObjectRequest request) {
                byte[] content = mockS3Storage.get(request.bucket() + request.key());
                if (content != null) {
                    return new ResponseInputStream<>(
                            GetObjectResponse.builder().build(),
                            new ByteArrayInputStream(content)
                    );
                }
                return null;
            }

            @Override
            public DeleteObjectResponse deleteObject(DeleteObjectRequest request) {
                mockS3Storage.remove(request.bucket() + request.key());
                return DeleteObjectResponse.builder().build();
            }
        };
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate() {
        return Mockito.mock(RabbitTemplate.class);
    }

    @Bean
    @Primary
    public StorageService storageService() {
        return Mockito.mock(StorageService.class);
    }
} 