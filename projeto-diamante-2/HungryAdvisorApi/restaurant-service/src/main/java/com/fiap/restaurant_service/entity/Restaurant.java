package com.fiap.hungryadvisor.restaurantservice.entity;

import java.util.UUID;

public record Restaurant(
        UUID id,
        String name,
        String category,
        String city,
        String priceRange,
        double rating
) {
}