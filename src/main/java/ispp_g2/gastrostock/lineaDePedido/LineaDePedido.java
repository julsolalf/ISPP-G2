package ispp_g2.gastrostock.lineaDePedido;


import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.pedido.Pedido;
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
public class LineaDePedido extends BaseEntity{
    
    @NotNull
    @Positive
    private Integer cantidad;

    @NotNull
    @Positive
    private Double precioLinea;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "producto_id")
    private ProductoVenta producto;

}
