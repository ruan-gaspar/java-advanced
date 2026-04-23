package com.fiap.hungryadvisor.userservice.service;

import com.fiap.hungryadvisor.userservice.dto.UserRequest;
import com.fiap.hungryadvisor.userservice.dto.UserResponse;
import com.fiap.hungryadvisor.userservice.entity.UserProfile;
import com.fiap.hungryadvisor.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse create(UserRequest request) {
        UserProfile user = new UserProfile(
                UUID.randomUUID(),
                request.name(),
                request.favoriteCuisine(),
                request.city(),
                request.priceRange()
        );

        repository.save(user);
        return toResponse(user);
    }

    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(UUID id) {
        UserProfile user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return toResponse(user);
    }

    private UserResponse toResponse(UserProfile user) {
        return new UserResponse(
                user.id(),
                user.name(),
                user.favoriteCuisine(),
                user.city(),
                user.priceRange()
        );
    }
}