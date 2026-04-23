package com.fiap.hungryadvisor.recommendationservice.service;

import com.fiap.hungryadvisor.recommendationservice.dto.RestaurantResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class RestaurantClientService {

    private final RestClient.Builder restClientBuilder;

    public RestaurantClientService(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public List<RestaurantResponse> search(String category, String city, Double minRating, String priceRange) {
        String uri = UriComponentsBuilder
                .fromUriString("http://restaurant-service/api/restaurants/search")
                .queryParamIfPresent("category", java.util.Optional.ofNullable(category))
                .queryParamIfPresent("city", java.util.Optional.ofNullable(city))
                .queryParamIfPresent("minRating", java.util.Optional.ofNullable(minRating))
                .queryParamIfPresent("priceRange", java.util.Optional.ofNullable(priceRange))
                .toUriString();

        RestaurantResponse[] response = restClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .body(RestaurantResponse[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.asList(response);
    }

    @Recover
    public List<RestaurantResponse> recover(Exception exception, String category, String city, Double minRating, String priceRange) {
        throw new IllegalStateException("Falha ao consultar restaurant-service após retries.", exception);
    }
}