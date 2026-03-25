package br.com.fiap.ApiTripFinder.controller;

import br.com.fiap.ApiTripFinder.dto.favorite.FavoritePlaceRequestDTO;
import br.com.fiap.ApiTripFinder.dto.favorite.FavoritePlaceResponseDTO;
import br.com.fiap.ApiTripFinder.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "Adicionar lugar aos favoritos")
    @PostMapping
    public FavoritePlaceResponseDTO addFavorite(
            Authentication authentication,
            @Valid @RequestBody FavoritePlaceRequestDTO request
    ) {
        return favoriteService.addFavorite(authentication.getName(), request);
    }

    @Operation(summary = "Listar favoritos do usuário")
    @GetMapping
    public List<FavoritePlaceResponseDTO> listFavorites(Authentication authentication) {
        return favoriteService.listFavorites(authentication.getName());
    }

    @Operation(summary = "Remover favorito")
    @DeleteMapping("/{externalPlaceId}")
    public void removeFavorite(
            Authentication authentication,
            @PathVariable String externalPlaceId
    ) {
        favoriteService.removeFavorite(authentication.getName(), externalPlaceId);
    }
}