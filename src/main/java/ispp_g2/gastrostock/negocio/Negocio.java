package ispp_g2.gastrostock.negocio;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Negocio extends NamedEntity {
    
    @NotNull
    @Column(unique = true)
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "dueno_id")
    private Dueno dueno;

}
