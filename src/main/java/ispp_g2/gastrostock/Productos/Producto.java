package ispp_g2.gastrostock.Productos;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "producto")
@Getter
@Setter
public class Producto extends NamedEntity {

    @Column(name = "fecha_caducidad")
    private LocalDate fechaCaducidad;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "descripcion")
    private Integer descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "medida")
    private Medida medida;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "ancho", column = @Column(name = "ancho")),
            @AttributeOverride(name = "profundidad", column = @Column(name = "profundidad")),
            @AttributeOverride(name = "alto", column = @Column(name = "alto"))
    })
    private Dimensiones tamaño;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToMany(mappedBy = "productos")
    private List<Plato> platos;

    @OneToMany(mappedBy = "producto")
    private List<ProductoAlmacen> productosAlmacenes;

}
