package com.fiap.hungryadvisor.recommendationservice.dto;

import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String category,
        String city,
        String priceRange,
        double rating
) {
}