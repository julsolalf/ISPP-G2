package ispp_g2.gastrostock.lineaDeCarrito;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineaDeCarritoDTO {

    private Integer id;
    @NotNull
    @Positive
    private Integer cantidad;
    private Double precioLinea;
    @NotNull
    private Integer productoId;
    @NotNull
    private Integer carritoId;

    public static LineaDeCarritoDTO of(LineaDeCarrito lineaDeCarrito) {
        LineaDeCarritoDTO lineaDeCarritoDTO = new LineaDeCarritoDTO();
        lineaDeCarritoDTO.setId(lineaDeCarrito.getId());
        lineaDeCarritoDTO.setCantidad(lineaDeCarrito.getCantidad());
        lineaDeCarritoDTO.setPrecioLinea(lineaDeCarrito.getPrecioLinea());
        lineaDeCarritoDTO.setProductoId(lineaDeCarrito.getProducto().getId());
        lineaDeCarritoDTO.setCarritoId(lineaDeCarrito.getCarrito().getId());
        return lineaDeCarritoDTO;
    }
    
}
