package kernel.tech.systemgab.utils.dto;

import kernel.tech.systemgab.utils.enums.StatutTransaction;
import kernel.tech.systemgab.utils.enums.TypeOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * DTO for table "TansactionDto"
 *
 * @author yeonoel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {


    private Long compteId;
    private Integer clientId;
    private TypeOperation typeOperation;
    private BigDecimal montant;
    private String message;

}

