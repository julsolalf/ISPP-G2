package ispp_g2.gastrostock.negocio;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NegocioDTO {

//    TO DO: AUTOGENERAR LOS TOKENS
//    CUANDO SE IMPLEMENTE CURR_USER AÑADIR VALIDACION PARA ASIGNAR AUTO LOS USUARIOS
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    private String direccion;

    @Digits(fraction = 0, integer = 5)
    private String codigoPostal;

    @NotBlank
    private String ciudad;

    @NotBlank
    private String pais;

    @NotNull
    private Integer tokenNegocio;

    @NotNull
    private Integer idDueno;
}
