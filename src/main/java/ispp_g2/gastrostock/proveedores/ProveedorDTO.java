package ispp_g2.gastrostock.proveedores;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorDTO {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Digits(fraction=0, integer=9)
    private String telefono;

    @NotBlank
    private String direccion;

    @NotNull
    private Integer negocioId;
}
