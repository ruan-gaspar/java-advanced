package com.fiap.hungryadvisor.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Favorite cuisine is required")
        String favoriteCuisine,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "Price range is required")
        String priceRange
) {
}