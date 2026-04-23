package com.fiap.hungryadvisor.restaurantservice.controller;

import com.fiap.hungryadvisor.restaurantservice.dto.RestaurantResponse;
import com.fiap.hungryadvisor.restaurantservice.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<RestaurantResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public RestaurantResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/search")
    public List<RestaurantResponse> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String priceRange
    ) {
        return service.search(category, city, minRating, priceRange);
    }
}