package br.com.fiap.ApiTripFinder.dto.favorite;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritePlaceResponseDTO {

    private Long id;
    private String externalPlaceId;
    private String name;
    private String category;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private LocalDateTime savedAt;
}