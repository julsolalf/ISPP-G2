package ispp_g2.gastrostock.dueño;

import ispp_g2.gastrostock.model.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dueño extends User {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String contraseña;

    @NotBlank
    private String numTlf;

    @NotBlank
    private String tokenDueño;

    // En Negocio tendría que haber una relación ManyToOne dirigida a Dueño.
    
}
