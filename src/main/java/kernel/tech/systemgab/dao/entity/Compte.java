package kernel.tech.systemgab.dao.entity;

import jakarta.persistence.*;
import kernel.tech.systemgab.utils.enums.TypeCompte;
import lombok.Data;

import java.math.BigDecimal;


/**
 * Persistent class for entity stored in table "comptes"
 *
 * @author yeonoel
 *
 */
@Data
@Entity
@Table(name = "comptes")
public class Compte {
    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compteId;
    private BigDecimal solde;

    @Enumerated(EnumType.STRING)
    private TypeCompte typeCompte;

    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}

