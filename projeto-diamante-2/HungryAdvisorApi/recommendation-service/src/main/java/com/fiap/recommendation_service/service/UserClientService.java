package com.fiap.hungryadvisor.recommendationservice.service;

import com.fiap.hungryadvisor.recommendationservice.dto.UserResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class UserClientService {

    private final RestClient.Builder restClientBuilder;

    public UserClientService(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public UserResponse getUserById(UUID userId) {
        return restClientBuilder.build()
                .get()
                .uri("http://user-service/api/users/{id}", userId)
                .retrieve()
                .body(UserResponse.class);
    }

    @Recover
    public UserResponse recover(Exception exception, UUID userId) {
        throw new IllegalStateException("Falha ao consultar user-service após retries. userId=" + userId, exception);
    }
}