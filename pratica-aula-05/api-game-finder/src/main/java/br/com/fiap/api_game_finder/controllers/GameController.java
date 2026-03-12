package br.com.fiap.api_game_finder.controllers;

import br.com.fiap.api_game_finder.models.GameResponse;
import br.com.fiap.api_game_finder.services.GameRecomendationService;
import br.com.fiap.api_game_finder.services.RawgApiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    private final RawgApiService rawgApiService;
    private final GameRecomendationService gameRecomendationService;

    public GameController(
            RawgApiService rawgApiService,
            GameRecomendationService gameRecomendationService
    ) {
        this.rawgApiService = rawgApiService;
        this.gameRecomendationService = gameRecomendationService;
    }

    public record GameResult(
            String result,
            double rating,
            int reviews,
            String released
    ) {}

    @GetMapping
    public GameResult recommend(@RequestParam String game) {

        var data = rawgApiService.getGame(game);
        var result = gameRecomendationService.evaluate(data);

        return new GameResult(
                result,
                data.rating(),
                data.ratings_count(),
                data.released()
        );
    }
}