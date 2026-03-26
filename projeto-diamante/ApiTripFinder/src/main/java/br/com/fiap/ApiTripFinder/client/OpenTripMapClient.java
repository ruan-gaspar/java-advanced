package br.com.fiap.ApiTripFinder.client;

import br.com.fiap.ApiTripFinder.dto.place.PlaceDetailDTO;
import br.com.fiap.ApiTripFinder.dto.place.PlaceSummaryDTO;
import br.com.fiap.ApiTripFinder.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OpenTripMapClient implements PlaceProviderClient {

    private final RestClient restClient;

    @Value("${opentripmap.api-key}")
    private String apiKey;

    private static final Set<String> BAD_KINDS = Set.of(
            "other",
            "interesting_places",
            "route",
            "paths",
            "urban_environment",
            "unclassified_objects"
    );

    public OpenTripMapClient(@Value("${opentripmap.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    @Override
    public List<PlaceSummaryDTO> searchPlaces(String query, String city, String category, Integer limit) {
        try {
            Map<String, Object> geoname = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/places/geoname")
                            .queryParam("name", city)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .body(Map.class);

            if (geoname == null || geoname.isEmpty()) {
                return List.of();
            }

            Double lat = asDouble(geoname.get("lat"));
            Double lon = asDouble(geoname.get("lon"));

            if (lat == null || lon == null) {
                return List.of();
            }

            List<PlaceSummaryDTO> places = searchNearby(
                    lat,
                    lon,
                    7000,
                    category,
                    limit != null ? limit * 3 : 30
            );

            return places.stream()
                    .filter(place -> matchesQuery(place, query))
                    .limit(limit != null ? limit : 10)
                    .toList();

        } catch (Exception e) {
            throw new BusinessException("Erro ao consultar a OpenTripMap: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceSummaryDTO> searchNearby(Double latitude, Double longitude, Integer radius, String category, Integer limit) {
        try {
            List<?> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/places/radius")
                            .queryParam("radius", radius != null ? radius : 3000)
                            .queryParam("lon", longitude)
                            .queryParam("lat", latitude)
                            .queryParam("limit", limit != null ? limit : 10)
                            .queryParam("rate", 2)
                            .queryParam("format", "json")
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .body(List.class);

            return mapRadiusResults(response, category);
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar lugares próximos na OpenTripMap: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceSummaryDTO> searchNearbyByTerm(Double latitude, Double longitude, Integer radius, String query, String category, Integer limit) {
        try {
            int safeLimit = limit != null ? limit : 10;
            int fetchLimit = Math.max(safeLimit * 3, 30);

            List<PlaceSummaryDTO> nearbyPlaces = searchNearby(
                    latitude,
                    longitude,
                    radius != null ? radius : 3000,
                    category,
                    fetchLimit
            );
            List<PlaceSummaryDTO> filtered = nearbyPlaces.stream()
                    .filter(place -> matchesQuery(place, query))
                    .toList();

            if (!filtered.isEmpty()) {
                return filtered.stream()
                        .limit(safeLimit)
                        .toList();
            }

            return nearbyPlaces.stream()
                    .limit(safeLimit)
                    .toList();

        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar lugares próximos para essa pesquisa: " + e.getMessage());
        }
    }

    @Override
    public PlaceDetailDTO getPlaceDetails(String placeId) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/places/xid/{xid}")
                            .queryParam("apikey", apiKey)
                            .build(placeId))
                    .retrieve()
                    .body(Map.class);

            Map<String, Object> address = safeMap(response.get("address"));
            Map<String, Object> point = safeMap(response.get("point"));
            Map<String, Object> preview = safeMap(response.get("preview"));

            return PlaceDetailDTO.builder()
                    .id(asString(response.get("xid")))
                    .name(asString(response.get("name")))
                    .description(extractDescription(response))
                    .category(formatKinds(asString(response.get("kinds"))))
                    .address(buildAddress(address))
                    .city(firstNonBlank(
                            asString(address.get("city")),
                            asString(address.get("town")),
                            asString(address.get("village")),
                            asString(address.get("state"))
                    ))
                    .country(asString(address.get("country")))
                    .latitude(asDouble(point.get("lat")))
                    .longitude(asDouble(point.get("lon")))
                    .website(extractWebsite(response))
                    .imageUrl(extractImageUrl(preview))
                    .phone(null)
                    .build();
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar detalhes do lugar na OpenTripMap: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<PlaceSummaryDTO> mapRadiusResults(List<?> response, String categoryFilter) {
        List<PlaceSummaryDTO> places = new ArrayList<>();

        if (response == null) {
            return places;
        }

        for (Object item : response) {
            if (!(item instanceof Map<?, ?> rawItem)) {
                continue;
            }

            Map<String, Object> place = (Map<String, Object>) rawItem;
            Map<String, Object> point = safeMap(place.get("point"));

            String name = asString(place.get("name"));
            String rawKinds = asString(place.get("kinds"));

            if (name == null || name.isBlank()) {
                continue;
            }

            if (isBadKind(rawKinds)) {
                continue;
            }

            if (!matchesCategory(rawKinds, categoryFilter)) {
                continue;
            }

            places.add(PlaceSummaryDTO.builder()
                    .id(asString(place.get("xid")))
                    .name(name)
                    .category(formatKinds(rawKinds))
                    .address(null)
                    .city(null)
                    .country(null)
                    .latitude(asDouble(point.get("lat")))
                    .longitude(asDouble(point.get("lon")))
                    .distance(null)
                    .imageUrl(null)
                    .build());
        }

        return places;
    }

    private boolean matchesQuery(PlaceSummaryDTO place, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }

        String normalizedQuery = normalize(query);
        String name = normalize(place.getName());
        String category = normalize(place.getCategory());

        // 🔥 sinônimos básicos
        if (normalizedQuery.contains("pizza") && category.contains("restaurant")) {
            return true;
        }

        if (normalizedQuery.contains("hamburguer") && category.contains("restaurant")) {
            return true;
        }

        if (normalizedQuery.contains("hotel") && category.contains("accommodation")) {
            return true;
        }

        if (normalizedQuery.contains("farmacia") && category.contains("shop")) {
            return true;
        }

        if (normalizedQuery.contains("museu") && category.contains("museum")) {
            return true;
        }

        return name.contains(normalizedQuery) || category.contains(normalizedQuery);
    }

    private boolean matchesCategory(String rawKinds, String categoryFilter) {
        if (categoryFilter == null || categoryFilter.isBlank()) {
            return true;
        }

        if (rawKinds == null || rawKinds.isBlank()) {
            return false;
        }

        String normalizedFilter = normalize(categoryFilter);

        List<String> kinds = Arrays.stream(rawKinds.split(","))
                .map(String::trim)
                .map(this::normalize)
                .toList();

        // 🔥 mapeamento inteligente
        if (normalizedFilter.equals("restaurants")) {
            return kinds.contains("restaurant")
                    || kinds.contains("fast_food")
                    || kinds.contains("catering")
                    || kinds.contains("cafe");
        }

        if (normalizedFilter.equals("accommodations")) {
            return kinds.contains("hotel")
                    || kinds.contains("hostel")
                    || kinds.contains("guest_house");
        }

        if (normalizedFilter.equals("museums")) {
            return kinds.contains("museum");
        }

        if (normalizedFilter.equals("shops")) {
            return kinds.contains("shop")
                    || kinds.contains("mall");
        }

        // fallback padrão
        return kinds.stream().anyMatch(k -> k.contains("restaurant"));
    }

    private boolean isBadKind(String rawKinds) {
        if (rawKinds == null || rawKinds.isBlank()) {
            return true;
        }

        List<String> kinds = Arrays.stream(rawKinds.split(","))
                .map(String::trim)
                .toList();

        return kinds.stream().allMatch(BAD_KINDS::contains);
    }

    private String formatKinds(String rawKinds) {
        if (rawKinds == null || rawKinds.isBlank()) {
            return null;
        }

        return Arrays.stream(rawKinds.split(","))
                .map(String::trim)
                .filter(kind -> !BAD_KINDS.contains(kind))
                .limit(3)
                .map(this::humanizeKind)
                .collect(Collectors.joining(", "));
    }

    private String humanizeKind(String kind) {
        return Arrays.stream(kind.split("_"))
                .map(word -> word.isBlank() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    private String extractDescription(Map<String, Object> response) {
        Map<String, Object> wikipediaExtracts = safeMap(response.get("wikipedia_extracts"));
        String text = asString(wikipediaExtracts.get("text"));
        if (text != null && !text.isBlank()) {
            return text;
        }

        Map<String, Object> info = safeMap(response.get("info"));
        return asString(info.get("descr"));
    }

    private String extractImageUrl(Map<String, Object> preview) {
        String source = asString(preview.get("source"));
        if (source != null && !source.isBlank()) {
            return source;
        }
        return null;
    }

    private String extractWebsite(Map<String, Object> response) {
        String url = asString(response.get("url"));
        if (url != null && !url.isBlank()) {
            return url;
        }

        Map<String, Object> otm = safeMap(response.get("otm"));
        return asString(otm.get("url"));
    }

    private String buildAddress(Map<String, Object> address) {
        List<String> parts = new ArrayList<>();

        addIfPresent(parts, asString(address.get("road")));
        addIfPresent(parts, asString(address.get("pedestrian")));
        addIfPresent(parts, asString(address.get("house_number")));
        addIfPresent(parts, firstNonBlank(
                asString(address.get("suburb")),
                asString(address.get("neighbourhood"))
        ));
        addIfPresent(parts, firstNonBlank(
                asString(address.get("city")),
                asString(address.get("town")),
                asString(address.get("village"))
        ));
        addIfPresent(parts, asString(address.get("state")));
        addIfPresent(parts, asString(address.get("country")));

        if (parts.isEmpty()) {
            return null;
        }

        return String.join(", ", parts);
    }

    private void addIfPresent(List<String> parts, String value) {
        if (value != null && !value.isBlank() && !parts.contains(value)) {
            parts.add(value);
        }
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
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

    private Double asDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return null;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}