package kernel.tech.systemgab.utils.dto;


import kernel.tech.systemgab.dao.entity.Client;
import kernel.tech.systemgab.utils.enums.TypeCompte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;



/**
 * DTO for Response after login
 *
 * @author yeonoel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long cardId;
    private Long clientId;
    private String nom;
    private String prenom;
    private TypeCompte typeCompte;
    private BigDecimal solde;

}
