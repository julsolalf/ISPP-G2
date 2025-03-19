package ispp_g2.gastrostock.pedido;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends CrudRepository<Pedido,String>{

    @Query("SELECT p FROM Pedido p WHERE p.fecha = ?1")
    List<Pedido> findPedidoByFecha(LocalDateTime fecha);

    @Query("SELECT p FROM Pedido p WHERE p.precioTotal = ?1")
    List<Pedido> findPedidoByPrecioTotal(Double precioTotal);

    @Query("SELECT p FROM Pedido p WHERE p.mesa.id = ?1")
    List<Pedido> findPedidoByMesaId(Integer mesa);

    @Query("SELECT p FROM Pedido p WHERE p.empleado.id = ?1")
    List<Pedido> findPedidoByEmpleadoId(Integer empleado);

    @Query("SELECT p FROM Pedido p WHERE p.negocio.id = ?1")
    List<Pedido> findPedidoByNegocioId(Integer negocio);
}
