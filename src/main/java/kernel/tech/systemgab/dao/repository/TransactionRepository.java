package kernel.tech.systemgab.dao.repository;

import kernel.tech.systemgab.dao.entity.Compte;
import kernel.tech.systemgab.dao.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository : TransactionRepository.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction>  findAllByCompte(Compte compte);
}
