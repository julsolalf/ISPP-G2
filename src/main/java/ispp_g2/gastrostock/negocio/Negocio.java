package ispp_g2.gastrostock.negocio;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Negocio extends NamedEntity {
    
    @NotNull
    private Integer tokenNegocio;
    @NotBlank
    private String direccion;
    @NotNull
	@Digits(fraction = 0, integer = 5)
    private String codigoPostal;
    @NotBlank
    private String ciudad;
    @NotBlank
    private String pais;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dueño_id")
    private Dueño dueño;
}
