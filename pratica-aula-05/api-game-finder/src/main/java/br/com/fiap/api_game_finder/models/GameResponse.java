package br.com.fiap.api_game_finder.models;

public class GameResponse {
    public record RawgResponse(
            double rating,
            int ratings_count,
            String released
    ) {}
}
