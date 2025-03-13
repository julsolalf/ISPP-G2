package ispp_g2.gastrostock.productoVenta;

import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductoVenta extends BaseEntity {

    @NotBlank
    private String nombre;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CategoriasVenta categoriaVenta;

    @NotNull
    private Double precioVenta;

    @ManyToOne
    @JoinColumn(name = "linea_pedido_id")
    private LineaDePedido lineaDePedido;

}
