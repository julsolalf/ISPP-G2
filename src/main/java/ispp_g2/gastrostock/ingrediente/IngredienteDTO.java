package ispp_g2.gastrostock.ingrediente;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredienteDTO {

    @NotNull
    private Integer cantidad;

    @NotNull
    private Integer productoInventarioId;

    @NotNull
    private Integer productoVentaId;

}
