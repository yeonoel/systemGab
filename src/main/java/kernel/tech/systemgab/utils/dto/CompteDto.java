package kernel.tech.systemgab.utils.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteDto {

    private BigDecimal NouveauSolde;
}
