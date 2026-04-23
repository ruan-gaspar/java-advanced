package com.fiap.hungryadvisor.recommendationservice.controller;

import com.fiap.hungryadvisor.recommendationservice.dto.RecommendationResponse;
import com.fiap.hungryadvisor.recommendationservice.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public RecommendationResponse recommend(@PathVariable UUID userId) {
        return service.recommend(userId, false);
    }

    @GetMapping("/{userId}/ai")
    public RecommendationResponse recommendWithAi(@PathVariable UUID userId) {
        return service.recommend(userId, true);
    }
}