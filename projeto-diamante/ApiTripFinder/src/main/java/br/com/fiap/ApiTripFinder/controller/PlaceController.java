package br.com.fiap.ApiTripFinder.controller;

import br.com.fiap.ApiTripFinder.dto.place.PlaceDetailDTO;
import br.com.fiap.ApiTripFinder.dto.place.PlaceSummaryDTO;
import br.com.fiap.ApiTripFinder.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PlaceController {

    private final PlaceService placeService;

    @Operation(summary = "Buscar lugares por termo e cidade")
    @GetMapping("/search")
    public List<PlaceSummaryDTO> searchPlaces(
            @RequestParam String query,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return placeService.searchPlaces(query, city, category, limit);
    }

    @Operation(summary = "Buscar lugares próximos por latitude e longitude")
    @GetMapping("/nearby")
    public List<PlaceSummaryDTO> searchNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "3000") Integer radius,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return placeService.searchNearby(latitude, longitude, radius, category, limit);
    }
    @Operation(summary = "Buscar lugares próximos por termos que não são cidade")
    @GetMapping("/nearby/search")
    public List<PlaceSummaryDTO> searchNearbyByTerm(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam (required = false) String query,
            @RequestParam(defaultValue = "3000") Integer radius,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") Integer limit
    ){
        return placeService.searchNearbyByTerm(latitude, longitude, radius, query, category, limit);
    }

    @Operation(summary = "Buscar detalhes de um lugar")
    @GetMapping("/{id}")
    public PlaceDetailDTO getPlaceDetails(@PathVariable String id) {
        return placeService.getPlaceDetails(id);
    }
}