package ispp_g2.gastrostock.diaReparto;

import java.time.DayOfWeek;

import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.negocio.Negocio;

import ispp_g2.gastrostock.proveedores.Proveedor;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
public class DiaReparto extends BaseEntity {

    @NotNull
    private DayOfWeek diaSemana;

    private String descripcion;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
    
}
