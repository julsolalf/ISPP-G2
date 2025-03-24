package ispp_g2.gastrostock.empleado;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.model.Person;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Empleado extends Person {

    @NotBlank
    @Column(unique = true)
    private String tokenEmpleado;

    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @OneToMany(mappedBy = "empleado",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;

    
}
