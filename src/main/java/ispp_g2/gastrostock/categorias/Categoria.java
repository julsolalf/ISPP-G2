package ispp_g2.gastrostock.categorias;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
public class Categoria extends NamedEntity {

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @NotNull
    private Pertenece pertenece;

}
