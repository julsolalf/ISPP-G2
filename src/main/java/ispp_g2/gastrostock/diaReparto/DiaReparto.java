package ispp_g2.gastrostock.diaReparto;

import java.time.DayOfWeek;

import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.negocio.Negocio;

import ispp_g2.gastrostock.proveedores.Proveedor;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DiaReparto extends BaseEntity {

    @NotNull
    private DayOfWeek diaSemana;

    private String descripcion;

    @ManyToOne
    @NotNull
    private Negocio negocio;

    @ManyToOne
    @NotNull
    private Proveedor proveedor;
    
}
