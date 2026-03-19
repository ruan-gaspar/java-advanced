package br.com.fiap.watchlist.repositories;

import br.com.fiap.watchlist.models.ToWatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToWatchRepository extends JpaRepository<ToWatch, Long> {
}
