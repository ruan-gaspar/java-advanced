package fiap.com.example.gameHub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RawgResponseDTO(
        List<fiap.com.example.gameHub.dto.RawgGameDTO> results
) {
}