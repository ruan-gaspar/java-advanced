package com.fiap.hungryadvisor.restaurantservice.repository;

import com.fiap.hungryadvisor.restaurantservice.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RestaurantRepository {

    private final Map<UUID, Restaurant> storage = new ConcurrentHashMap<>();

    public Restaurant save(Restaurant restaurant) {
        storage.put(restaurant.id(), restaurant);
        return restaurant;
    }

    public Optional<Restaurant> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Restaurant> findAll() {
        return storage.values()
                .stream()
                .sorted(Comparator.comparing(Restaurant::name))
                .toList();
    }

    public long count() {
        return storage.size();
    }
}