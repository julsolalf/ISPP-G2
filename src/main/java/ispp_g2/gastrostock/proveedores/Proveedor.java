package ispp_g2.gastrostock.proveedores;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Proveedor extends NamedEntity {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "negocio_id", nullable = false)
    private Negocio negocio;

}
