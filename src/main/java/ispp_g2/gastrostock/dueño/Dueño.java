package ispp_g2.gastrostock.dueño;

import ispp_g2.gastrostock.model.Person;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dueño extends Person {

    @NotBlank
    @Column(unique = true)
    private String tokenDueño;


}
