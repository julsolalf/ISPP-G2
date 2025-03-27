package ispp_g2.gastrostock.productoVenta;

import ispp_g2.gastrostock.categorias.Categoria;
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
public class ProductoVenta extends NamedEntity {

    @NotNull
    private Double precioVenta;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
