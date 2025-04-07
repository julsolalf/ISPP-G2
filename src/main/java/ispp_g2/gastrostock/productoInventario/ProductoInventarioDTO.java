package ispp_g2.gastrostock.productoInventario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoInventarioDTO {

    @NotBlank
    private String name;

    @NotNull
    private Double precioCompra;

    @NotNull
    private Integer cantidadDeseada;

    @NotNull
    private Integer cantidadAviso;

    @NotNull
    private Integer categoriaId;


}
