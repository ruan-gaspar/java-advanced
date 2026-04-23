package com.fiap.hungryadvisor.recommendationservice.service;

import com.fiap.hungryadvisor.recommendationservice.dto.RestaurantResponse;
import com.fiap.hungryadvisor.recommendationservice.dto.UserResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiSuggestionService {

    private final ChatClient chatClient;

    public AiSuggestionService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateSuggestion(UserResponse user, List<RestaurantResponse> restaurants) {
        if (restaurants == null || restaurants.isEmpty()) {
            return "Não há restaurantes suficientes para gerar uma recomendação por IA.";
        }

        String prompt = """
                Você é um assistente especialista em recomendação de restaurantes.

                Perfil do usuário:
                - Nome: %s
                - Culinária preferida: %s
                - Cidade: %s
                - Faixa de preço: %s

                Restaurantes candidatos:
                %s

                Gere uma recomendação curta em português do Brasil, com no máximo 6 linhas.
                Explique qual restaurante parece mais adequado e cite até 3 opções.
                """.formatted(
                user.name(),
                user.favoriteCuisine(),
                user.city(),
                user.priceRange(),
                formatRestaurants(restaurants)
        );

        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception ex) {
            return "A IA não conseguiu responder agora. A recomendação baseada em regras continua válida.";
        }
    }

    private String formatRestaurants(List<RestaurantResponse> restaurants) {
        return restaurants.stream()
                .map(restaurant -> "- %s | categoria=%s | cidade=%s | preço=%s | nota=%.1f"
                        .formatted(
                                restaurant.name(),
                                restaurant.category(),
                                restaurant.city(),
                                restaurant.priceRange(),
                                restaurant.rating()
                        ))
                .collect(Collectors.joining("\n"));
    }
}