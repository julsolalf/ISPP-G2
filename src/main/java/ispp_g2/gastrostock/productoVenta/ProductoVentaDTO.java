package ispp_g2.gastrostock.productoVenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoVentaDTO {

    @NotBlank
    private String name;

    @NotNull
    private Double precioVenta;

    @NotNull
    private Integer categoriaId;
}
