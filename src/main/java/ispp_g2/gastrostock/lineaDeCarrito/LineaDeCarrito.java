package ispp_g2.gastrostock.lineaDeCarrito;

import ispp_g2.gastrostock.carrito.Carrito;
import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class LineaDeCarrito extends BaseEntity {

    @NotNull
    @Positive
    private Integer cantidad;

    @NotNull
    private Double precioLinea;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "producto_id")
    private ProductoInventario producto;

}
