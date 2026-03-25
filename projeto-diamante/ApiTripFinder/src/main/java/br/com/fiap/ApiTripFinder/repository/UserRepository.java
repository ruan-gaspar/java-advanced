package br.com.fiap.ApiTripFinder.repository;

import br.com.fiap.ApiTripFinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    String Email(String email);
}
