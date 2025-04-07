package ispp_g2.gastrostock.pedido;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDto {

    private Integer id;

    @NotNull
    @PastOrPresent
    private LocalDateTime fecha;

    @NotNull
    @Positive
    private Double precioTotal;

    @NotNull
    private Integer mesaId;

    private Integer empleadoId;

    @NotNull
    private Integer ventaId;

}
