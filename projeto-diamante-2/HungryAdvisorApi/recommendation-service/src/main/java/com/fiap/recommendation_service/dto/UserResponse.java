package com.fiap.hungryadvisor.recommendationservice.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String favoriteCuisine,
        String city,
        String priceRange
) {
}