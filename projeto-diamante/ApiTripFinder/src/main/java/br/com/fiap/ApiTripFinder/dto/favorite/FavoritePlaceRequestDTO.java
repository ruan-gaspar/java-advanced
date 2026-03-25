package br.com.fiap.ApiTripFinder.dto.favorite;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritePlaceRequestDTO {

    @NotBlank(message = "O id do lugar é obrigatório")
    private String id;

    @NotBlank(message = "O nome do lugar é obrigatório")
    private String name;

    private String category;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
}