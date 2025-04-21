package ispp_g2.gastrostock.diaReparto;

import java.time.DayOfWeek;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaRepartoDTO {

    private Integer id;

    @NotNull
    private DayOfWeek diaSemana;

    private String descripcion;

    @NotNull
    private Integer proveedorId;

    private String nombreProveedor;

    public static DiaRepartoDTO of(DiaReparto diaReparto) {
        DiaRepartoDTO diaRepartoDTO = new DiaRepartoDTO();
        diaRepartoDTO.setId(diaReparto.getId());
        diaRepartoDTO.setDiaSemana(diaReparto.getDiaSemana()); 
        diaRepartoDTO.setDescripcion(diaReparto.getDescripcion());
        diaRepartoDTO.setProveedorId(diaReparto.getProveedor().getId());
        diaRepartoDTO.setNombreProveedor(diaReparto.getProveedor().getName());
        return diaRepartoDTO;
    }
    
}
