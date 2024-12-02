package kernel.tech.systemgab.dao.repository;

import kernel.tech.systemgab.dao.entity.Carte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository : ClientRepository.
 */
@Repository
public interface CarteRepository extends JpaRepository<Carte, Long> {
    Optional<Carte> findByNumeroCarte(String numeroCarte);
}
