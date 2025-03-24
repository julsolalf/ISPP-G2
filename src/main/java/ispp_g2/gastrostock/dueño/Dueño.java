package ispp_g2.gastrostock.dueño;

import ispp_g2.gastrostock.model.Person;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.User;
import jakarta.persistence.*;
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
public class Dueño extends Person {

    @NotBlank
    @Column(unique = true)
    private String tokenDueño;

    @OneToMany(mappedBy = "dueño", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Negocio> negocios;

}
