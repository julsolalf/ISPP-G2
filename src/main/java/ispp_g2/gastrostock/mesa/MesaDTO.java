package ispp_g2.gastrostock.mesa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MesaDTO {

    private Integer id;

    @NotBlank
    private String nombre;
    @NotNull
    @Min(2)
    private Integer numeroAsientos;
    @NotNull
    private Integer negocioId;

    private String nombreNegocio;

    public static MesaDTO of(Mesa mesa) {
        MesaDTO mesaDTO = new MesaDTO();
        mesaDTO.setId(mesa.getId());
        mesaDTO.setNombre(mesa.getName());
        mesaDTO.setNumeroAsientos(mesa.getNumeroAsientos());
        mesaDTO.setNegocioId(mesa.getNegocio().getId());
        mesaDTO.setNombreNegocio(mesa.getNegocio().getName());
        return mesaDTO; 
    }

}
