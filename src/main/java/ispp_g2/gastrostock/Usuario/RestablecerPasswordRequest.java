package ispp_g2.gastrostock.Usuario;

import jakarta.validation.constraints.NotBlank;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RestablecerPasswordRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String codigo;

    @NotBlank
    private String nuevaContrasena;


}
