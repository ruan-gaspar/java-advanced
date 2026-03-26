package br.com.fiap.ApiTripFinder.service;

import br.com.fiap.ApiTripFinder.client.PlaceProviderClient;
import br.com.fiap.ApiTripFinder.dto.place.PlaceDetailDTO;
import br.com.fiap.ApiTripFinder.dto.place.PlaceSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceProviderClient placeProviderClient;

    public List<PlaceSummaryDTO> searchPlaces(String query, String city, String category, Integer limit) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Informe a cidade para realizar a busca por termo.");
        }

        return placeProviderClient.searchPlaces(query, city, category, limit);
    }

    public List<PlaceSummaryDTO> searchNearby(Double latitude, Double longitude, Integer radius, String category, Integer limit) {
        return placeProviderClient.searchNearby(latitude, longitude, radius, category, limit);
    }

    public List<PlaceSummaryDTO> searchNearbyByTerm(Double latitude, Double longitude, Integer radius, String query, String category, Integer limit
    ) {
        return placeProviderClient.searchNearbyByTerm(latitude, longitude, radius, query, category, limit);
    }

    public PlaceDetailDTO getPlaceDetails(String placeId) {
        return placeProviderClient.getPlaceDetails(placeId);
    }
}