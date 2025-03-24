package ispp_g2.gastrostock.proveedores;

import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

}
