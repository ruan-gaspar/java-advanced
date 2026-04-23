package com.fiap.hungryadvisor.recommendationservice.service;

import com.fiap.hungryadvisor.recommendationservice.dto.RecommendationResponse;
import com.fiap.hungryadvisor.recommendationservice.dto.RestaurantResponse;
import com.fiap.hungryadvisor.recommendationservice.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {

    private final UserClientService userClientService;
    private final RestaurantClientService restaurantClientService;
    private final AiSuggestionService aiSuggestionService;

    public RecommendationService(
            UserClientService userClientService,
            RestaurantClientService restaurantClientService,
            AiSuggestionService aiSuggestionService
    ) {
        this.userClientService = userClientService;
        this.restaurantClientService = restaurantClientService;
        this.aiSuggestionService = aiSuggestionService;
    }

    public RecommendationResponse recommend(UUID userId, boolean includeAi) {
        UserResponse user = userClientService.getUserById(userId);

        List<RestaurantResponse> candidates = restaurantClientService.search(
                user.favoriteCuisine(),
                user.city(),
                4.0,
                user.priceRange()
        );

        if (candidates.isEmpty()) {
            candidates = restaurantClientService.search(
                    user.favoriteCuisine(),
                    user.city(),
                    null,
                    user.priceRange()
            );
        }

        if (candidates.isEmpty()) {
            candidates = restaurantClientService.search(
                    null,
                    user.city(),
                    4.0,
                    null
            );
        }

        List<RestaurantResponse> selected = candidates.stream()
                .sorted(Comparator.comparing(RestaurantResponse::rating).reversed())
                .limit(3)
                .toList();

        String ruleBasedExplanation = buildRuleBasedExplanation(user, selected);

        String aiSuggestion = includeAi
                ? aiSuggestionService.generateSuggestion(user, selected)
                : null;

        return new RecommendationResponse(user, selected, ruleBasedExplanation, aiSuggestion);
    }

    private String buildRuleBasedExplanation(UserResponse user, List<RestaurantResponse> restaurants) {
        if (restaurants.isEmpty()) {
            return "Nenhum restaurante foi encontrado com base nas preferências do usuário.";
        }

        return """
                Restaurantes selecionados com base na preferência culinária "%s", cidade "%s",
                faixa de preço "%s" e priorização por melhor avaliação.
                """.formatted(
                user.favoriteCuisine(),
                user.city(),
                user.priceRange()
        ).replace("\n", " ").trim();
    }
}