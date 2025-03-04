package ispp_g2.gastrostock.Proveedores;

import java.util.List;

import ispp_g2.gastrostock.Productos.Producto;
import ispp_g2.gastrostock.model.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
public class Proveedor extends Person {

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "direccion")
    private String direccion;

    @OneToMany(mappedBy = "proveedor")
    private List<Producto> productos;

    
}
