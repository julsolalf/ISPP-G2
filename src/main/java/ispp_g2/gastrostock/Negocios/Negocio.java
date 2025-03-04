package ispp_g2.gastrostock.Negocios;

import java.util.List;

import ispp_g2.gastrostock.Proveedores.Proveedor;
import ispp_g2.gastrostock.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "negocios")
@Getter
@Setter
public class Negocio extends NamedEntity {

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "cif_nif", unique = true, nullable = false)
    private String cifNif;

    // Relación con Proveedores (Un negocio puede tener muchos proveedores)
    @OneToMany
    @JoinColumn(name = "negocio_id")
    private List<Proveedor> proveedores;
    
}
