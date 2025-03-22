package ispp_g2.gastrostock.lote;

import java.time.LocalDate;

import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Lote extends BaseEntity {

    @NotNull
    @Positive
    private Integer cantidad;

    @NotNull
    private LocalDate fechaCaducidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private ProductoInventario producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reabastecimiento_id")
    private Reabastecimiento reabastecimiento;

}
