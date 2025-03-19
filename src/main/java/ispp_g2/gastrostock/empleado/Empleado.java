package ispp_g2.gastrostock.empleado;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.model.Person;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Empleado extends Person {

    @NotBlank
    private String tokenEmpleado;

    private String descripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    
}
