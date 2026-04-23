package br.com.fiap.ApiTripFinder.service;

import br.com.fiap.ApiTripFinder.client.PlaceProviderClient;
import br.com.fiap.ApiTripFinder.dto.place.PlaceDetailDTO;
import br.com.fiap.ApiTripFinder.dto.place.PlaceSummaryDTO;
import br.com.fiap.ApiTripFinder.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceProviderClient placeProviderClient;

    public List<PlaceSummaryDTO> searchPlaces(String query, String city, String category, Integer limit) {
        validateQuery(query);

        if (city == null || city.isBlank()) {
            throw new BusinessException("A cidade é obrigatória para a busca por cidade.");
        }

        validateLimit(limit);

        return placeProviderClient.searchPlaces(
                query.trim(),
                city.trim(),
                normalizeOptional(category),
                normalizeLimit(limit)
        );
    }

    public List<PlaceSummaryDTO> searchNearby(Double latitude, Double longitude, Integer radius, String category, Integer limit) {
        validateCoordinates(latitude, longitude);
        validateRadius(radius);
        validateLimit(limit);

        return placeProviderClient.searchNearby(
                latitude,
                longitude,
                normalizeRadius(radius),
                normalizeOptional(category),
                normalizeLimit(limit)
        );
    }

    public List<PlaceSummaryDTO> searchNearbyByTerm(Double latitude, Double longitude, Integer radius, String query, String category, Integer limit) {
        validateCoordinates(latitude, longitude);
        validateQuery(query);
        validateRadius(radius);
        validateLimit(limit);

        return placeProviderClient.searchNearbyByTerm(
                latitude,
                longitude,
                normalizeRadius(radius),
                query.trim(),
                normalizeOptional(category),
                normalizeLimit(limit)
        );
    }

    public PlaceDetailDTO getPlaceDetails(String placeId) {
        if (placeId == null || placeId.isBlank()) {
            throw new BusinessException("O identificador do lugar é obrigatório.");
        }

        return placeProviderClient.getPlaceDetails(placeId.trim());
    }

    private void validateQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new BusinessException("O termo de busca é obrigatório.");
        }
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new BusinessException("Latitude e longitude são obrigatórias.");
        }

        if (latitude < -90 || latitude > 90) {
            throw new BusinessException("Latitude inválida.");
        }

        if (longitude < -180 || longitude > 180) {
            throw new BusinessException("Longitude inválida.");
        }
    }

    private void validateRadius(Integer radius) {
        if (radius != null && radius <= 0) {
            throw new BusinessException("O raio deve ser maior que zero.");
        }
    }

    private void validateLimit(Integer limit) {
        if (limit != null && limit <= 0) {
            throw new BusinessException("O limite deve ser maior que zero.");
        }

        if (limit != null && limit > 50) {
            throw new BusinessException("O limite máximo permitido é 50.");
        }
    }

    private String normalizeOptional(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private Integer normalizeLimit(Integer limit) {
        return (limit == null || limit <= 0) ? 10 : limit;
    }

    private Integer normalizeRadius(Integer radius) {
        return (radius == null || radius <= 0) ? 3000 : radius;
    }
}