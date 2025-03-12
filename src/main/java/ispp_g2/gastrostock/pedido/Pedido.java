package ispp_g2.gastrostock.pedido;

import java.time.LocalDateTime;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pedido extends BaseEntity{

    @NotNull
    @PastOrPresent
    private LocalDateTime fecha;

    @NotNull
    @Positive
    private Double precioTotal;

//    TEMPORAL FIX
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "id")
//    private Mesa mesa;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "id")
//    private Empleado empleado;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "id")
//    private Negocio negocio;
}
