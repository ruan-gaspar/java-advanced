package br.com.fiap.api_game_finder.models;

public class GameResponse {
    public record RawgResponse(
            String name,
            double rating,
            int ratings_count,
            String released,
            String background_image
    ) {}
}
