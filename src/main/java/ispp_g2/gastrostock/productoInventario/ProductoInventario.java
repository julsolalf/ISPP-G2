package ispp_g2.gastrostock.productoInventario;

import java.util.List;
import java.util.stream.Collectors;


import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class ProductoInventario extends NamedEntity {

    @NotNull
    private Double precioCompra;

    @NotNull
    private Integer cantidadDeseada;

    @NotNull
    private Integer cantidadAviso;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Transient
    public Integer calcularCantidad(List<Lote> lotes) {
        return lotes.stream().collect(Collectors.summingInt(Lote::getCantidad));
    }

    
}
