package br.com.fiap.ApiTripFinder.dto.place;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDetailDTO {

    private String id;
    private String name;
    private String description;
    private String category;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String website;
    private String imageUrl;
}