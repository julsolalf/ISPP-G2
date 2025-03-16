package ispp_g2.gastrostock.mesa;

import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Mesa extends NamedEntity{
    
    @NotNull
    @Min(2)
    private Integer numeroAsientos;

    @ManyToOne(optional = false)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;
}
