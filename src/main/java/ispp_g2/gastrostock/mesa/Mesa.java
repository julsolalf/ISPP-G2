package ispp_g2.gastrostock.mesa;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Mesa extends NamedEntity{
    
    private Integer numeroMesa;

    private Integer numeroAsientos;

    @ManyToOne(optional = false)
    private Negocio negocio;
}
