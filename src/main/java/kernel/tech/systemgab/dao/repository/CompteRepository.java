package kernel.tech.systemgab.dao.repository;

import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.dao.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository : CompteRepository.
 */
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByCompteIdAndClient(Long compteId, Client client);

    Compte findByClient(Client client);
}
