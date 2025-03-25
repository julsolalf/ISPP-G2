package ispp_g2.gastrostock.negocio;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @JoinColumn(name = "dueño_id")
    private Dueño dueño;

}
