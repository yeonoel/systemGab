package kernel.tech.systemgab.dao.repository;

import kernel.tech.systemgab.dao.entity.Compte;
import kernel.tech.systemgab.dao.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByCompte(Compte compte);

    List<Transaction>  findAllByCompte(Compte compte);
}
