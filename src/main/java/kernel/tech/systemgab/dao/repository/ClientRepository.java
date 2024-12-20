package kernel.tech.systemgab.dao.repository;

import kernel.tech.systemgab.dao.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository : ClientRepository.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
