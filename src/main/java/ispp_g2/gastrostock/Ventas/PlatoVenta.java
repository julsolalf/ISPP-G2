package ispp_g2.gastrostock.Ventas;


import ispp_g2.gastrostock.plato.Plato;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlatoVenta {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "plato_id")
    private Plato plato;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    private Integer cantidad;


}
