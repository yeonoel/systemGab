package kernel.tech.systemgab.dao.repository;

import kernel.tech.systemgab.dao.entity.Carte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteRepository extends JpaRepository<Carte, Long> {
    Optional<Carte> findByNumeroCarte(String numeroCarte);
}
