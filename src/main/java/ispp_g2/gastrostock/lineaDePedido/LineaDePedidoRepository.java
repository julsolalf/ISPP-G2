package ispp_g2.gastrostock.lineaDePedido;

import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineaDePedidoRepository extends CrudRepository<LineaDePedido,Integer> {

    @Query("SELECT l FROM LineaDePedido l WHERE l.cantidad = ?1")
    List<LineaDePedido>findLineaDePedidosByCantidad(@Positive Integer cantidad);

    @Query("SELECT l FROM LineaDePedido l WHERE l.precioUnitario = ?1")
    List<LineaDePedido>findLineaDePedidosByPrecioUnitario(@Positive Double precioUnitario);

    @Query("SELECT l FROM LineaDePedido l WHERE l.pedido.id = ?1")
    List<LineaDePedido> findLineaDePedidosByPedidoId(@Positive Integer pedido);

    @Query("SELECT l FROM LineaDePedido l WHERE l.producto.id = ?1")
    List<LineaDePedido> findLineaDePedidosByProductoId(@Positive Integer producto);

    @Query("SELECT l FROM LineaDePedido l WHERE l.producto.id = ?1 AND l.cantidad = ?2")
    List<LineaDePedido> findLineaDePedidosByProductoIdAndCantidad(@Positive Integer producto, @Positive Integer cantidad);

    @Query("SELECT l FROM LineaDePedido l WHERE l.producto.id = ?1 AND l.precioUnitario = ?2")
    List<LineaDePedido> findLineaDePedidosByProductoIdAndPrecioUnitario(Integer producto, @Positive Double precioUnitario);

}
