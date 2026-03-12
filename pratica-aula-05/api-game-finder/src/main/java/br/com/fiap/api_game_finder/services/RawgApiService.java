package br.com.fiap.api_game_finder.services;

import br.com.fiap.api_game_finder.models.GameResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RawgApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    public GameResponse.RawgResponse getGame (String game) {
        String url = "https://api.rawg.io/api/games/"
                + game
                + "?key=3de3ff32ed1542b5a9ae59b3490ef3fe";
        return restTemplate.getForObject(url, GameResponse.RawgResponse.class);
    }
}
