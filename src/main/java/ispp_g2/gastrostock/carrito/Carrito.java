package ispp_g2.gastrostock.carrito;

import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.proveedores.Proveedor;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Carrito extends BaseEntity {


    @NotNull
    @Min(0)
    private Double precioTotal;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @NotNull
    private LocalDate diaEntrega;
}
