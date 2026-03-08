package fiap.com.example.gameHub.services;

import org.springframework.stereotype.Service;

@Service
public class RawgClient {

    private final WebClient webClient;

    public RawgClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.rawg.io/api")
                .build();
    }

    public String buscarJogo(String nome) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("search", nome)
                        .queryParam("key", "SUA_API_KEY")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}