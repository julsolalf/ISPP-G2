package ispp_g2.gastrostock.Almacenes;

import java.util.List;

import ispp_g2.gastrostock.Productos.ProductoAlmacen;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "metrosCuadrados")
    private Double metrosCuadrados;

    @OneToMany(mappedBy = "almacen")
    private List<ProductoAlmacen> productosAlmacenes;
}
