package br.com.fiap.ApiTripFinder.dto.place;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceSummaryDTO {

    private String id;
    private String name;
    private String category;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Integer distance;
    private String imageUrl;
}