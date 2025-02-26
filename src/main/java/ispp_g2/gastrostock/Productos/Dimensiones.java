package ispp_g2.gastrostock.Productos;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
class Dimensiones {
    private double ancho;
    private double alto;
    private double profundidad;
}
