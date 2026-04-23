package com.fiap.hungryadvisor.recommendationservice.dto;

import java.util.List;

public record RecommendationResponse(
        UserResponse user,
        List<RestaurantResponse> restaurants,
        String ruleBasedExplanation,
        String aiSuggestion
) {
}