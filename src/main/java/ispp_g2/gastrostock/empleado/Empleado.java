package ispp_g2.gastrostock.empleado;

import ispp_g2.gastrostock.model.User;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Empleado extends User {

    @NotBlank
    private String tokenEmpleado;

    @NotNull
    private Rol rol;

    @ManyToOne
    @NotNull
    private Negocio negocio;

    // En Pedido tendría que haber una relación ManyToOne a Empleado.
    
}
