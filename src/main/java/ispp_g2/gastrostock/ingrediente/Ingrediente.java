package ispp_g2.gastrostock.ingrediente;

import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoVenta.ProductoVenta;
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
public class Ingrediente extends BaseEntity{

    @NotNull
    @Positive
    private Integer cantidad;
    

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "producto_inventario_id")
    private ProductoInventario productoInventario;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "producto_venta_id")
    private ProductoVenta productoVenta;
}
