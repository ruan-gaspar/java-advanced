package br.com.fiap.ApiTripFinder.service;

import br.com.fiap.ApiTripFinder.dto.favorite.FavoritePlaceRequestDTO;
import br.com.fiap.ApiTripFinder.dto.favorite.FavoritePlaceResponseDTO;
import br.com.fiap.ApiTripFinder.entity.FavoritePlace;
import br.com.fiap.ApiTripFinder.entity.User;
import br.com.fiap.ApiTripFinder.exception.BusinessException;
import br.com.fiap.ApiTripFinder.exception.ResourceNotFoundException;
import br.com.fiap.ApiTripFinder.repository.FavoritePlaceRepository;
import br.com.fiap.ApiTripFinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoritePlaceRepository favoritePlaceRepository;
    private final UserRepository userRepository;

    public FavoritePlaceResponseDTO addFavorite(String userEmail, FavoritePlaceRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        favoritePlaceRepository.findByUserIdAndExternalPlaceId(user.getId(), request.getExternalPlaceId())
                .ifPresent(favorite -> {
                    throw new BusinessException("Esse lugar já está nos favoritos");
                });

        FavoritePlace favoritePlace = FavoritePlace.builder()
                .externalPlaceId(request.getExternalPlaceId())
                .name(request.getName())
                .category(request.getCategory())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .imageUrl(request.getImageUrl())
                .savedAt(LocalDateTime.now())
                .user(user)
                .build();

        FavoritePlace saved = favoritePlaceRepository.save(favoritePlace);

        return mapToResponse(saved);
    }

    public List<FavoritePlaceResponseDTO> listFavorites(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return favoritePlaceRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void removeFavorite(String userEmail, String externalPlaceId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        FavoritePlace favorite = favoritePlaceRepository
                .findByUserIdAndExternalPlaceId(user.getId(), externalPlaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito não encontrado"));

        favoritePlaceRepository.delete(favorite);
    }

    private FavoritePlaceResponseDTO mapToResponse(FavoritePlace favorite) {
        return FavoritePlaceResponseDTO.builder()
                .id(favorite.getId())
                .externalPlaceId(favorite.getExternalPlaceId())
                .name(favorite.getName())
                .category(favorite.getCategory())
                .address(favorite.getAddress())
                .city(favorite.getCity())
                .country(favorite.getCountry())
                .latitude(favorite.getLatitude())
                .longitude(favorite.getLongitude())
                .imageUrl(favorite.getImageUrl())
                .savedAt(favorite.getSavedAt())
                .build();
    }
}