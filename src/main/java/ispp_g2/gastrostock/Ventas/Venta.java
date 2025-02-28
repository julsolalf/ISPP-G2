package ispp_g2.gastrostock.Ventas;

import java.time.LocalDate;
import java.util.List;

import ispp_g2.gastrostock.Productos.Producto;
import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.plato.Plato;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "venta")
@Getter
@Setter
public class Venta extends BaseEntity {

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @ManyToMany
    @JoinTable(
        name = "venta_plato", 
        joinColumns = @JoinColumn(name = "venta_id"), 
        inverseJoinColumns = @JoinColumn(name = "plato_id")
    )
    private List<Plato> productos;  

    @Column(name = "total")
    private Double total;

    
}
