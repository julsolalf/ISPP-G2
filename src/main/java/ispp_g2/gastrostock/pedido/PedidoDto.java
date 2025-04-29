package ispp_g2.gastrostock.pedido;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDto {

    private Integer id;

    @PastOrPresent
    private LocalDateTime fecha;

    @NotNull
    @Min(0)
    private Double precioTotal;

    @NotNull
    private Integer mesaId;

    private Integer empleadoId;

    @NotNull
    private Integer negocioId;

}
