package kernel.tech.systemgab.dao.entity;

import jakarta.persistence.*;
import kernel.tech.systemgab.utils.enums.TypeCompte;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "comptes")
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compteId;
    private BigDecimal solde;

    @Enumerated(EnumType.STRING)
    private TypeCompte typeCompte;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Getters, setters, constructeurs
}

