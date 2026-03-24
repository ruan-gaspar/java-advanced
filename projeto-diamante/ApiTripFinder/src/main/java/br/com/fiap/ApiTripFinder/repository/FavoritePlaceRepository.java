package br.com.fiap.ApiTripFinder.repository;

import br.com.fiap.ApiTripFinder.entity.FavoritePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, Long> {
    List<FavoritePlace> findByUserId(Long userId);
    Optional<FavoritePlace> findByUserIdAndExternalPlaceId(Long userId, String externalPlaceId);
    void deleteByUserIdAndExternalPlaceId(Long userId, String externalPlaceId);
}

