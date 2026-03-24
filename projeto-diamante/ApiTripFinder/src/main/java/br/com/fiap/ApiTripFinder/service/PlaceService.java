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
        return placeProviderClient.searchPlaces(query, city, category, limit);
    }

    public List<PlaceSummaryDTO> searchNearby(Double latitude, Double longitude, Integer radius, String category, Integer limit) {
        return placeProviderClient.searchNearby(latitude, longitude, radius, category, limit);
    }

    public PlaceDetailDTO getPlaceDetails(String placeId) {
        return placeProviderClient.getPlaceDetails(placeId);
    }
}