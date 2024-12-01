package kernel.tech.systemgab.utils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

        private String nom;
        private String prenom;
        private String password;
        private String typedeCOmpte;
        private String cardNumero;

}