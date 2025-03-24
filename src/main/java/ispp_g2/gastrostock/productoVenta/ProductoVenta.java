package ispp_g2.gastrostock.productoVenta;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Getter
@Setter
public class ProductoVenta extends NamedEntity {

    @NotNull
    private Double precioVenta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaDePedido> lineasDePedido;

    @OneToMany(mappedBy = "productoVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingrediente> ingredientes;

}
