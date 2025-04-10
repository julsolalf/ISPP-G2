package ispp_g2.gastrostock.lineaDePedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineaDePedidoDTO {

    private Integer id;
    @NotNull
    @Positive
    private Integer cantidad;
    @NotNull
    @Positive
    private Double precioUnitario;
    
    private Double precioLinea;

    @NotNull
    private Boolean estado;
    @NotNull
    private Integer pedidoId;
    @NotBlank
    private String nombreProducto;

    private String categoriaProducto;

    public static LineaDePedidoDTO of(LineaDePedido lineaDePedido) {
        LineaDePedidoDTO res = new LineaDePedidoDTO();
        res.setId(lineaDePedido.getId());
        res.setCantidad(lineaDePedido.getCantidad());
        res.setPrecioUnitario(lineaDePedido.getPrecioUnitario());
        res.setPrecioLinea(lineaDePedido.getPrecioLinea());
        res.setEstado(lineaDePedido.getSalioDeCocina());
        res.setPedidoId(lineaDePedido.getPedido().getId());
        res.setNombreProducto(lineaDePedido.getProducto().getName());
        res.setCategoriaProducto(lineaDePedido.getProducto().getCategoria().getName());

        return res;
    }
    
}
