package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductoInventario extends BaseEntity {

    @NotBlank
    private String nombre;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CategoriasInventario categoriaInventario;

    @NotNull
    private Double precioCompra;

    public int calcularCantidad() {
        return lotes.stream().mapToInt(Lote::getCantidad).sum();
    }

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private List<Lote> lotes;
}
