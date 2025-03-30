package ispp_g2.gastrostock.carrito;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends CrudRepository<Carrito, Integer> {

    @Query("SELECT c FROM Carrito c WHERE c.proveedor.id = ?1")
    List<Carrito> findByProveedorId(Integer id);

    @Query("SELECT c FROM Carrito c WHERE c.negocio.id = ?1")
    List<Carrito> findByNegocioId(Integer id);
}
