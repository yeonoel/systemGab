package kernel.tech.systemgab.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    private String nom;
    private String prenom;
    private String password;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
