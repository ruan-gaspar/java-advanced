package com.fiap.hungryadvisor.restaurantservice.service;

import com.fiap.hungryadvisor.restaurantservice.dto.RestaurantResponse;
import com.fiap.hungryadvisor.restaurantservice.entity.Restaurant;
import com.fiap.hungryadvisor.restaurantservice.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public List<RestaurantResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RestaurantResponse findById(UUID id) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

        return toResponse(restaurant);
    }

    public List<RestaurantResponse> search(String category, String city, Double minRating, String priceRange) {
        return repository.findAll()
                .stream()
                .filter(restaurant -> category == null || restaurant.category().equalsIgnoreCase(category))
                .filter(restaurant -> city == null || restaurant.city().equalsIgnoreCase(city))
                .filter(restaurant -> minRating == null || restaurant.rating() >= minRating)
                .filter(restaurant -> priceRange == null || restaurant.priceRange().equalsIgnoreCase(priceRange))
                .sorted(Comparator.comparing(Restaurant::rating).reversed())
                .map(this::toResponse)
                .toList();
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.id(),
                restaurant.name(),
                restaurant.category(),
                restaurant.city(),
                restaurant.priceRange(),
                restaurant.rating()
        );
    }
}