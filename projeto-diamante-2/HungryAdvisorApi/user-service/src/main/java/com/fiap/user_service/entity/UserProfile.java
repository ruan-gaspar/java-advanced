package com.fiap.hungryadvisor.userservice.entity;

import java.util.UUID;

public record UserProfile(
        UUID id,
        String name,
        String favoriteCuisine,
        String city,
        String priceRange
) {
}