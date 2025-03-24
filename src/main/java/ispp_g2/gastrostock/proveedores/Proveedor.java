package ispp_g2.gastrostock.proveedores;

import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Proveedor extends NamedEntity {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Digits(fraction = 0, integer = 9)
    private String telefono;

    @NotBlank
    private String direccion;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaReparto> diasReparto;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reabastecimiento> reabastecimientos;
}
