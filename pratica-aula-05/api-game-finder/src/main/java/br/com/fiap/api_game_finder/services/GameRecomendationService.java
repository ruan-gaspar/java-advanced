package br.com.fiap.api_game_finder.services;

import br.com.fiap.api_game_finder.models.GameResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GameRecomendationService {

    public String evaluate(GameResponse.RawgResponse game) {

        double rating = game.rating();
        int reviews = game.ratings_count();

        int year = LocalDate.parse(game.released()).getYear();
        int currentYear = LocalDate.now().getYear();

        int age = currentYear - year;

        if (rating >= 4.5 && reviews > 1000 && age <= 10) {
            return "Altamente recomendado";
        }

        if (rating >= 3.5 && rating < 4.5) {
            return "Vale a pena";
        }

        return "Melhor ver um filme";
    }
}