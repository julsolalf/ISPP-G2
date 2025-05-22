package ispp_g2.gastrostock.pedido;

import java.time.LocalDateTime;


import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Pedido extends BaseEntity{

    
    @PastOrPresent
    private LocalDateTime fecha;

    @NotNull
    @Min(0)
    private Double precioTotal;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

}
