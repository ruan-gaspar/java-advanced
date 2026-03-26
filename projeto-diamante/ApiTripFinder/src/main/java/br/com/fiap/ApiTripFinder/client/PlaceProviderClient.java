package br.com.fiap.ApiTripFinder.client;

import br.com.fiap.ApiTripFinder.dto.place.PlaceDetailDTO;
import br.com.fiap.ApiTripFinder.dto.place.PlaceSummaryDTO;

import java.util.List;

public interface PlaceProviderClient {

    List<PlaceSummaryDTO> searchPlaces(String query, String city, String category, Integer limit);

    List<PlaceSummaryDTO> searchNearby(Double latitude, Double longitude, Integer radius, String category, Integer limit);

    List<PlaceSummaryDTO> searchNearbyByTerm(Double latitude, Double longitude, Integer radius, String query, String category, Integer limit);

    PlaceDetailDTO getPlaceDetails(String placeId);
}