package br.com.fiap.ApiTripFinder.client;

import br.com.fiap.ApiTripFinder.dto.place.PlaceDetailDTO;
import br.com.fiap.ApiTripFinder.dto.place.PlaceSummaryDTO;
import br.com.fiap.ApiTripFinder.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FoursquareClient implements PlaceProviderClient {

    private final RestClient restClient;

    @Value("${foursquare.api-key}")
    private String apiKey;

    public FoursquareClient(@Value("${foursquare.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    @Override
    public List<PlaceSummaryDTO> searchPlaces(String query, String city, String category, Integer limit) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/places/search");

                        if (query != null && !query.isBlank()) {
                            uriBuilder.queryParam("query", query);
                        }

                        if (city != null && !city.isBlank()) {
                            uriBuilder.queryParam("near", city);
                        }

                        if (limit != null) {
                            uriBuilder.queryParam("limit", limit);
                        }

                        return uriBuilder.build();
                    })
                    .header("Authorization", apiKey)
                    .retrieve()
                    .body(Map.class);

            return mapSearchResults(response);
        } catch (Exception e) {
            throw new BusinessException("Erro ao consultar a API da Foursquare: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceSummaryDTO> searchNearby(Double latitude, Double longitude, Integer radius, String category, Integer limit) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/places/search");

                        if (latitude != null && longitude != null) {
                            uriBuilder.queryParam("ll", latitude + "," + longitude);
                        }

                        if (radius != null) {
                            uriBuilder.queryParam("radius", radius);
                        }

                        if (limit != null) {
                            uriBuilder.queryParam("limit", limit);
                        }

                        return uriBuilder.build();
                    })
                    .header("Authorization", apiKey)
                    .retrieve()
                    .body(Map.class);

            return mapSearchResults(response);
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar lugares próximos: " + e.getMessage());
        }
    }

    @Override
    public PlaceDetailDTO getPlaceDetails(String placeId) {
        throw new UnsupportedOperationException("Detalhes do lugar serão implementados no próximo passo.");
    }

    @SuppressWarnings("unchecked")
    private List<PlaceSummaryDTO> mapSearchResults(Map<String, Object> response) {
        List<PlaceSummaryDTO> places = new ArrayList<>();

        Object resultsObj = response.get("results");
        if (!(resultsObj instanceof List<?> results)) {
            return places;
        }

        for (Object item : results) {
            if (!(item instanceof Map<?, ?> rawItem)) {
                continue;
            }

            Map<String, Object> place = (Map<String, Object>) rawItem;

            String fsqId = asString(place.get("fsq_id"));
            String name = asString(place.get("name"));
            Integer distance = asInteger(place.get("distance"));

            Map<String, Object> location = safeMap(place.get("location"));
            Map<String, Object> geocodes = safeMap(place.get("geocodes"));
            Map<String, Object> main = safeMap(geocodes.get("main"));

            String address = asString(location.get("formatted_address"));
            String country = asString(location.get("country"));
            String city = asString(location.get("locality"));

            Double latitude = asDouble(main.get("latitude"));
            Double longitude = asDouble(main.get("longitude"));

            String category = null;
            Object categoriesObj = place.get("categories");
            if (categoriesObj instanceof List<?> categories && !categories.isEmpty()) {
                Object first = categories.get(0);
                if (first instanceof Map<?, ?> rawCategory) {
                    Map<String, Object> categoryMap = (Map<String, Object>) rawCategory;
                    category = asString(categoryMap.get("name"));
                }
            }

            places.add(PlaceSummaryDTO.builder()
                    .id(fsqId)
                    .name(name)
                    .category(category)
                    .address(address)
                    .city(city)
                    .country(country)
                    .latitude(latitude)
                    .longitude(longitude)
                    .distance(distance)
                    .imageUrl(null)
                    .build());
        }

        return places;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> safeMap(Object value) {
        if (value instanceof Map<?, ?> rawMap) {
            return (Map<String, Object>) rawMap;
        }
        return new LinkedHashMap<>();
    }

    private String asString(Object value) {
        return value != null ? String.valueOf(value) : null;
    }

    private Integer asInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }

    private Double asDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return null;
    }
}