package ispp_g2.gastrostock.plato;

import ispp_g2.gastrostock.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Plato")
public class Plato extends NamedEntity {
    
    private String descripcion;
    @NotNull
    private Double precio;
    @NotBlank
    private String categoria;
    @NotNull
    public Cantidad cantidad;
}