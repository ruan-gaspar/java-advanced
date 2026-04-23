package com.fiap.hungryadvisor.restaurantservice.config;

import com.fiap.hungryadvisor.restaurantservice.entity.Restaurant;
import com.fiap.hungryadvisor.restaurantservice.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RestaurantSeedConfig {

    @Bean
    CommandLineRunner seedRestaurants(RestaurantRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            repository.save(new Restaurant(
                    UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                    "Cantinho Brasileiro",
                    "Brasileira",
                    "São Paulo",
                    "MEDIO",
                    4.7
            ));

            repository.save(new Restaurant(
                    UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                    "Sushi Prime",
                    "Japonesa",
                    "São Paulo",
                    "ALTO",
                    4.9
            ));

            repository.save(new Restaurant(
                    UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                    "Mamma Mia Trattoria",
                    "Italiana",
                    "Campinas",
                    "BAIXO",
                    4.6
            ));

            repository.save(new Restaurant(
                    UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"),
                    "Tokyo Garden",
                    "Japonesa",
                    "São Paulo",
                    "MEDIO",
                    4.5
            ));

            repository.save(new Restaurant(
                    UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"),
                    "Forno da Vila",
                    "Italiana",
                    "São Paulo",
                    "MEDIO",
                    4.4
            ));

            repository.save(new Restaurant(
                    UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"),
                    "Sabor do Interior",
                    "Brasileira",
                    "Campinas",
                    "BAIXO",
                    4.3
            ));

            repository.save(new Restaurant(
                    UUID.fromString("99999999-9999-9999-9999-999999999999"),
                    "Bistrô Central",
                    "Contemporânea",
                    "São Paulo",
                    "ALTO",
                    4.8
            ));
        };
    }
}