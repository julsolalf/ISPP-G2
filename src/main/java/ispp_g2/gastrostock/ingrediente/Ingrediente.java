package ispp_g2.gastrostock.ingrediente;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Getter
@Setter
public class Ingrediente extends BaseEntity{

    @NotNull
    @Positive
    private Integer cantidad;
    

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_inventario_id")
    private ProductoInventario productoInventario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_venta_id")
    private ProductoVenta productoVenta;
}
