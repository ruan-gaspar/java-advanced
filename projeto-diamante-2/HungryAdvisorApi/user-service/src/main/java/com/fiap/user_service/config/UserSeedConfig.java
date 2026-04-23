package com.fiap.hungryadvisor.userservice.config;

import com.fiap.hungryadvisor.userservice.entity.UserProfile;
import com.fiap.hungryadvisor.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class UserSeedConfig {

    @Bean
    CommandLineRunner seedUsers(UserRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            repository.save(new UserProfile(
                    UUID.fromString("11111111-1111-1111-1111-111111111111"),
                    "João Silva",
                    "Brasileira",
                    "São Paulo",
                    "MEDIO"
            ));

            repository.save(new UserProfile(
                    UUID.fromString("22222222-2222-2222-2222-222222222222"),
                    "Marina Costa",
                    "Japonesa",
                    "São Paulo",
                    "ALTO"
            ));

            repository.save(new UserProfile(
                    UUID.fromString("33333333-3333-3333-3333-333333333333"),
                    "Carlos Lima",
                    "Italiana",
                    "Campinas",
                    "BAIXO"
            ));
        };
    }
}