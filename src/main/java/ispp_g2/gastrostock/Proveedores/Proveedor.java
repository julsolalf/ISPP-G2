package ispp_g2.gastrostock.proveedores;

import java.util.List;

import ispp_g2.gastrostock.model.Person;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Proveedor extends Person {

    private String email;
    private String telefono;
    private String direccion;

    @ElementCollection
    @Enumerated(EnumType.STRING) 
    private List<DiaSemana> diasReparto;
    
    @OneToMany(mappedBy = "proveedor")
    private List<Reabastecimiento> reabastecimientos;

    @ManyToMany(mappedBy = "proveedores")
    private List<Negocio> negocios;
}
