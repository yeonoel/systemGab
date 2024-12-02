package kernel.tech.systemgab.utils.dto;

import kernel.tech.systemgab.utils.enums.StatutTransaction;
import kernel.tech.systemgab.utils.enums.TypeOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTransactionDto {

    private TypeOperation typeOperation;
    private BigDecimal montant;
    private String dateTransaction;
    private StatutTransaction statut;
    private String message;
}
