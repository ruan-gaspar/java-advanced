package com.fiap.hungryadvisor.restaurantservice.dto;

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