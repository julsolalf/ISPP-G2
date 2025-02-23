package ispp_g2.gastrostock.Productos;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "producto")
@Getter
@Setter
public class Producto extends NamedEntity {

    @Column(name = "fecha_caducidad")
    private LocalDate fechaCaducidad;

    @Column(name = "cantidad")
    private Integer cantidad;

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
}
