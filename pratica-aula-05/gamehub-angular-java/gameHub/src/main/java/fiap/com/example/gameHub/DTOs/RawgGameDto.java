package fiap.com.example.gameHub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawgGameDTO(
        String name,
        double rating,
        int metacritic,
        int ratings_count
) {
}