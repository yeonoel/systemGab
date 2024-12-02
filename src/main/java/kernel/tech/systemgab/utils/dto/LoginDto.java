package kernel.tech.systemgab.utils.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for request login
 *
 * @author yeonoel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String cardNumber;
    private String password;
}
