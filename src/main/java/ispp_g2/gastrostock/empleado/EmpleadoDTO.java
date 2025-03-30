package ispp_g2.gastrostock.empleado;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpleadoDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Digits(fraction = 0, integer = 9)
    private String numTelefono;

    @NotBlank
    private String tokenEmpleado;

    private String descripcion;

    @NotBlank
    private Integer user;

    @NotBlank
    private Integer negocio;

}
