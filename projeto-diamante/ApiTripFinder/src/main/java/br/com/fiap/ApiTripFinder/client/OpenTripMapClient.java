package br.com.fiap.ApiTripFinder.client;

import br.com.fiap.ApiTripFinder.dto.place.PlaceDetailDTO;
import br.com.fiap.ApiTripFinder.dto.place.PlaceSummaryDTO;
import br.com.fiap.ApiTripFinder.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OpenTripMapClient implements PlaceProviderClient {

    private final RestClient restClient;

    @Value("${opentripmap.api-key}")
    private String apiKey;

    private static final Set<String> BAD_KINDS = Set.of(
            "other",
            "route",
            "paths",
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
        if (query == null || query.isBlank()) {
            throw new BusinessException("O termo de busca é obrigatório.");
        }

        if (city == null || city.isBlank()) {
            throw new BusinessException("A cidade é obrigatória para a busca por cidade.");
        }

        try {
            Map<String, Object> geoname = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/places/geoname")
                            .queryParam("name", city.trim())
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

            int safeLimit = normalizeLimit(limit);
            int fetchLimit = Math.max(safeLimit * 6, 60);

            List<PlaceSummaryDTO> places = searchNearby(
                    lat,
                    lon,
                    15000,
                    category,
                    fetchLimit
            );

            if (category != null && !category.isBlank()) {
                return places.stream()
                        .limit(safeLimit)
                        .toList();
            }

            return places.stream()
                    .filter(place -> matchesQuery(place, query))
                    .limit(safeLimit)
                    .toList();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao consultar a OpenTripMap: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceSummaryDTO> searchNearby(Double latitude, Double longitude, Integer radius, String category, Integer limit) {
        if (latitude == null || longitude == null) {
            throw new BusinessException("Latitude e longitude são obrigatórias.");
        }

        try {
            List<?> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/places/radius")
                            .queryParam("radius", normalizeRadius(radius))
                            .queryParam("lon", longitude)
                            .queryParam("lat", latitude)
                            .queryParam("limit", normalizeLimit(limit))
                            .queryParam("rate", 2)
                            .queryParam("format", "json")
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .body(List.class);

            return mapRadiusResults(response, category);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar lugares próximos na OpenTripMap: " + e.getMessage());
        }
    }

    @Override
    public List<PlaceSummaryDTO> searchNearbyByTerm(Double latitude, Double longitude, Integer radius, String query, String category, Integer limit) {
        if (latitude == null || longitude == null) {
            throw new BusinessException("Latitude e longitude são obrigatórias.");
        }

        if (query == null || query.isBlank()) {
            throw new BusinessException("O termo de busca é obrigatório.");
        }

        try {
            int safeLimit = normalizeLimit(limit);
            int fetchLimit = Math.max(safeLimit * 6, 60);
            String effectiveCategory = (category != null && !category.isBlank()) ? category : query;

            List<PlaceSummaryDTO> nearbyPlaces = searchNearby(
                    latitude,
                    longitude,
                    normalizeRadius(radius),
                    effectiveCategory,
                    fetchLimit
            );

            if (isSupportedCategoryQuery(query)) {
                return nearbyPlaces.stream()
                        .limit(safeLimit)
                        .toList();
            }

            return nearbyPlaces.stream()
                    .filter(place -> matchesQuery(place, query))
                    .limit(safeLimit)
                    .toList();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar lugares próximos para essa pesquisa: " + e.getMessage());
        }
    }

    @Override
    public PlaceDetailDTO getPlaceDetails(String placeId) {
        if (placeId == null || placeId.isBlank()) {
            throw new BusinessException("O identificador do lugar é obrigatório.");
        }

        try {
            Map<String, Object> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/places/xid/{xid}")
                            .queryParam("apikey", apiKey)
                            .build(placeId))
                    .retrieve()
                    .body(Map.class);

            if (response == null || response.isEmpty()) {
                throw new BusinessException("Lugar não encontrado.");
            }

            Map<String, Object> address = safeMap(response.get("address"));
            Map<String, Object> point = safeMap(response.get("point"));
            Map<String, Object> preview = safeMap(response.get("preview"));

            return PlaceDetailDTO.builder()
                    .id(asString(response.get("xid")))
                    .name(firstNonBlank(asString(response.get("name")), "Local sem nome"))
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

        } catch (BusinessException e) {
            throw e;
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

            String xid = asString(place.get("xid"));
            String name = asString(place.get("name"));
            String rawKinds = asString(place.get("kinds"));

            if (xid == null || xid.isBlank()) {
                continue;
            }

            if (name == null || name.isBlank()) {
                continue;
            }

            if (isBadKind(rawKinds)) {
                continue;
            }

            if (!matchesCategory(rawKinds, categoryFilter)) {
                continue;
            }
            if (name == null || name.isBlank()) {
                continue;
            }

            places.add(PlaceSummaryDTO.builder()
                    .id(xid)
                    .name(name)
                    .category(formatKinds(rawKinds))
                    .address(null)
                    .city(null)
                    .country(null)
                    .latitude(asDouble(point.get("lat")))
                    .longitude(asDouble(point.get("lon")))
                    .distance(asDouble(place.get("dist")))
                    .imageUrl(null)
                    .build());
        }

        return places;
    }
    private boolean isInvalidPlaceName(String name) {
        if (name == null || name.isBlank()) {
            return true;
        }

        String normalized = normalize(name);

        return normalized.matches(".*painel\\s*\\d+.*")
                || normalized.matches(".*panel\\s*\\d+.*")
                || normalized.contains("sculpture")
                || normalized.contains("statue")
                || normalized.contains("memorial plaque")
                || normalized.contains("plaqueta")
                || normalized.contains("detail")
                || normalized.contains("fragment")
                || normalized.length() < 3;
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

        return switch (normalizedFilter) {
            case "museum", "museu" ->
                    containsAnyPart(kinds, "museum", "museums", "art_galleries", "cultural");

            case "theatre", "teatro" ->
                    containsAnyPart(kinds, "theatre", "theatres", "entertainments", "cultural", "music_venues");

            case "cinema" ->
                    containsAnyPart(kinds, "cinema", "cinemas", "entertainments", "cultural");

            case "hotel", "hostel", "hospedagem" ->
                    containsAnyPart(kinds, "hotel", "hotels", "hostel", "hostels", "accommodation", "accomodation", "apartments", "guest_house", "motel");

            case "church", "igreja", "cathedral", "catedral" ->
                    containsAnyPart(kinds, "church", "churches", "cathedral", "cathedrals", "religion", "chapel", "chapels");

            case "park", "parque" ->
                    containsAnyPart(kinds, "park", "parks", "gardens", "gardens_and_parks", "natural", "botanical_gardens");

            case "monument", "monumento" ->
                    containsAnyPart(kinds, "monument", "monuments", "memorial", "memorials", "sculpture", "sculptures");

            case "shop", "shopping" ->
                    containsAnyPart(kinds, "shop", "shops", "mall", "malls", "market", "marketplaces", "supermarkets");

            case "historic", "historico", "histórico" ->
                    containsAnyPart(kinds, "historic", "historic_architecture", "archaeology", "fortification", "fortifications", "palaces");

            default ->
                    kinds.stream().anyMatch(k -> k.contains(normalizedFilter));
        };
    }

    private boolean matchesQuery(PlaceSummaryDTO place, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }

        String normalizedQuery = normalize(query);
        String name = normalize(place.getName());
        String category = normalize(place.getCategory());
        String address = normalize(place.getAddress());
        String city = normalize(place.getCity());

        return switch (normalizedQuery) {
            case "museum", "museu" ->
                    containsAnyText(name, category, address, city, "museum", "museu", "art", "gallery");

            case "theatre", "teatro" ->
                    containsAnyText(name, category, address, city, "theatre", "teatro", "cultural", "entertainment");

            case "cinema" ->
                    containsAnyText(name, category, address, city, "cinema", "movie", "entertainment", "theatre");

            case "hotel", "hostel", "hospedagem" ->
                    containsAnyText(name, category, address, city, "hotel", "hostel", "accommodation", "guest", "motel");

            case "church", "igreja", "cathedral", "catedral" ->
                    containsAnyText(name, category, address, city, "church", "igreja", "cathedral", "catedral", "religion", "chapel");

            case "park", "parque" ->
                    containsAnyText(name, category, address, city, "park", "parque", "garden", "natural");

            case "monument", "monumento" ->
                    containsAnyText(name, category, address, city, "monument", "monumento", "memorial", "historic");

            case "shop", "shopping" ->
                    containsAnyText(name, category, address, city, "shop", "shopping", "mall", "market");

            case "historic", "historico", "histórico" ->
                    containsAnyText(name, category, address, city, "historic", "historico", "histórico", "architecture", "archaeology");

            default ->
                    name.contains(normalizedQuery)
                            || category.contains(normalizedQuery)
                            || address.contains(normalizedQuery)
                            || city.contains(normalizedQuery);
        };
    }

    private boolean isSupportedCategoryQuery(String query) {
        String normalized = normalize(query);

        return Set.of(
                "museum", "museu",
                "theatre", "teatro",
                "cinema",
                "hotel", "hostel", "hospedagem",
                "church", "igreja", "cathedral", "catedral",
                "park", "parque",
                "monument", "monumento",
                "shop", "shopping",
                "historic", "historico", "histórico"
        ).contains(normalized);
    }

    private boolean containsAnyText(String name, String category, String address, String city, String... terms) {
        List<String> fields = List.of(name, category, address, city);

        return Arrays.stream(terms)
                .map(this::normalize)
                .anyMatch(term -> fields.stream().anyMatch(field -> field.contains(term)));
    }

    private boolean containsAnyPart(List<String> values, String... expectedParts) {
        List<String> normalizedExpected = Arrays.stream(expectedParts)
                .map(this::normalize)
                .toList();

        return values.stream().anyMatch(value ->
                normalizedExpected.stream().anyMatch(value::contains)
        );
    }

    private boolean containsAny(List<String> values, String... expected) {
        Set<String> expectedSet = Arrays.stream(expected)
                .map(this::normalize)
                .collect(Collectors.toSet());

        return values.stream().anyMatch(expectedSet::contains);
    }

    private boolean isBadKind(String rawKinds) {
        if (rawKinds == null || rawKinds.isBlank()) {
            return true;
        }

        List<String> kinds = Arrays.stream(rawKinds.split(","))
                .map(String::trim)
                .map(this::normalize)
                .toList();

        return kinds.stream().allMatch(BAD_KINDS::contains);
    }

    private String formatKinds(String rawKinds) {
        if (rawKinds == null || rawKinds.isBlank()) {
            return null;
        }

        List<String> kinds = Arrays.stream(rawKinds.split(","))
                .map(String::trim)
                .map(this::normalize)
                .toList();

        if (containsAnyPart(kinds, "museum", "museums", "art_galleries")) {
            return "Museus";
        }

        if (containsAnyPart(kinds, "cinema", "cinemas")) {
            return "Cinemas";
        }

        if (containsAnyPart(kinds, "theatre", "theatres", "entertainments")) {
            return "Teatros e cultura";
        }

        if (containsAnyPart(kinds, "hotel", "hotels", "hostel", "hostels", "accommodation", "accomodation", "apartments", "motel")) {
            return "Hotéis";
        }

        if (containsAnyPart(kinds, "church", "churches", "cathedral", "cathedrals", "religion", "chapel")) {
            return "Igrejas";
        }

        if (containsAnyPart(kinds, "park", "parks", "gardens", "gardens_and_parks", "natural")) {
            return "Parques";
        }

        if (containsAnyPart(kinds, "monument", "monuments", "memorial", "memorials", "sculpture", "sculptures")) {
            return "Monumentos";
        }

        if (containsAnyPart(kinds, "shop", "shops", "mall", "malls", "market", "marketplaces")) {
            return "Compras e lazer";
        }

        if (containsAnyPart(kinds, "historic", "historic_architecture", "archaeology", "fortification", "palaces")) {
            return "Pontos históricos";
        }

        return Arrays.stream(rawKinds.split(","))
                .map(String::trim)
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
        return (source != null && !source.isBlank()) ? source : null;
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

        String street = firstNonBlank(
                asString(address.get("road")),
                asString(address.get("pedestrian")),
                asString(address.get("footway")),
                asString(address.get("street"))
        );

        String houseNumber = asString(address.get("house_number"));

        if (street != null && houseNumber != null && !houseNumber.isBlank()) {
            addIfPresent(parts, street + ", " + houseNumber);
        } else {
            addIfPresent(parts, street);
        }

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

        return parts.isEmpty() ? null : String.join(", ", parts);
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

    private Integer normalizeLimit(Integer limit) {
        return (limit == null || limit <= 0) ? 10 : limit;
    }

    private Integer normalizeRadius(Integer radius) {
        return (radius == null || radius <= 0) ? 3000 : radius;
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

        if (value instanceof String text) {
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").trim().toLowerCase(Locale.ROOT);
    }
}