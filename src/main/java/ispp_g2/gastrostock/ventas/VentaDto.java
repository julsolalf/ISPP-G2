package ispp_g2.gastrostock.ventas;

import ispp_g2.gastrostock.pedido.PedidoRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class VentaDto {
    
    private Integer id;

    @NotNull
    @Positive
    private Double precioTotal;

    @NotNull
    private Integer negocioId;

    
}
