package com.fiap.hungryadvisor.userservice.repository;

import com.fiap.hungryadvisor.userservice.entity.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private final Map<UUID, UserProfile> storage = new ConcurrentHashMap<>();

    public UserProfile save(UserProfile userProfile) {
        storage.put(userProfile.id(), userProfile);
        return userProfile;
    }

    public Optional<UserProfile> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<UserProfile> findAll() {
        return storage.values()
                .stream()
                .sorted(Comparator.comparing(UserProfile::name))
                .toList();
    }

    public long count() {
        return storage.size();
    }
}