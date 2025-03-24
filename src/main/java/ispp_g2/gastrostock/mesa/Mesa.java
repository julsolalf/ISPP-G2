package ispp_g2.gastrostock.mesa;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.pedido.Pedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Mesa extends NamedEntity{
    
    @NotNull
    @Min(2)
    private Integer numeroAsientos;

    @ManyToOne(optional = false)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @OneToMany(mappedBy = "mesa",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;
}
