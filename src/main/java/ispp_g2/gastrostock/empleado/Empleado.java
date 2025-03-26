package ispp_g2.gastrostock.empleado;


import ispp_g2.gastrostock.model.Person;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;



@Entity
@Getter
@Setter
public class Empleado extends Person {

    @NotBlank
    @Column(unique = true)
    private String tokenEmpleado;

    private String descripcion;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    
}
