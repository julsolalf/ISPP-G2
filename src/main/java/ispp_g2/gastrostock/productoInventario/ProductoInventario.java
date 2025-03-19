package ispp_g2.gastrostock.productoInventario;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductoInventario extends NamedEntity {

    @Enumerated(EnumType.STRING)
    @NotNull
    private CategoriasInventario categoriaInventario;

    @NotNull
    private Double precioCompra;

    @NotNull
    private Integer cantidadDeseada;

    @NotNull
    private Integer cantidadAviso;

    @Transient
    public Integer calcularCantidad(List<Lote> lotes) {
        return lotes.stream().collect(Collectors.summingInt(Lote::getCantidad));
    }
    
}
