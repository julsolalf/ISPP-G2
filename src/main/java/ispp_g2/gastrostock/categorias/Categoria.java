package ispp_g2.gastrostock.categorias;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Categoria extends NamedEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

}
