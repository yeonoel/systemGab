package kernel.tech.systemgab.dao.entity;

import jakarta.persistence.*;
import kernel.tech.systemgab.utils.enums.StatutTransaction;
import kernel.tech.systemgab.utils.enums.TypeOperation;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Persistent class for entity stored in table "transactions"
 *
 * @author yeonoel
 */
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "compte_id")
    private Compte compte;
    @Enumerated(EnumType.STRING)
    private TypeOperation typeOperation;
    private BigDecimal montant;
    private LocalDateTime dateTransaction;
    @Enumerated(EnumType.STRING)
    private StatutTransaction statut;
    private String message;
}
