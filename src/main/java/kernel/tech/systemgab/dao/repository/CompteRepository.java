package kernel.tech.systemgab.dao.repository;

import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.dao.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByCompteIdAndClient(Long compteId, Client client);
}
