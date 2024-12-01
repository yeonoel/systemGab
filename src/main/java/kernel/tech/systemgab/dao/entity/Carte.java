package kernel.tech.systemgab.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "cartes")
public class Carte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carteId;
    @Column(name = "numero_carte")
    private String numeroCarte;
    private String password;
    private LocalDate dateExpiration;
    private boolean estActive;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}