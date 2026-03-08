package pet_shop_api.API.de.Doguinhos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pet_shop_api.API.de.Doguinhos.models.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
