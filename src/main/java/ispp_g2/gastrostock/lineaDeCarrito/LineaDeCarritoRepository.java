package ispp_g2.gastrostock.lineaDeCarrito;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineaDeCarritoRepository extends CrudRepository<LineaDeCarrito, Integer> {

    @Query("SELECT l FROM LineaDeCarrito l WHERE l.carrito.id = ?1")
    List<LineaDeCarrito> findLineaDeCarritoByCarritoId(Integer id);

    @Query("SELECT l FROM LineaDeCarrito l WHERE l.carrito.id = ?1 AND l.producto.id = ?2")
    List<LineaDeCarrito> findLineaDeCarritoByCarritoIdAndProductoId(Integer idCarrito, Integer idProducto);

}
