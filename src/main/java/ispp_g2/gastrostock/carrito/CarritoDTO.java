package ispp_g2.gastrostock.carrito;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarritoDTO {

    private Integer id;
    private Double precioTotal;
    private LocalDate fechaEntrega;
    @NotNull
    private Integer proveedorId;


    public static CarritoDTO of(Carrito carrito) {
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.id = carrito.getId();
        carritoDTO.precioTotal = carrito.getPrecioTotal();
        carritoDTO.fechaEntrega = carrito.getDiaEntrega();
        carritoDTO.proveedorId = carrito.getProveedor().getId();
        return carritoDTO;
    }
    
}
