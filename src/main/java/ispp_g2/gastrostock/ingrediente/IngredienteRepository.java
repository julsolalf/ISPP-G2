package ispp_g2.gastrostock.ingrediente;

import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredienteRepository extends CrudRepository<Ingrediente,Integer>{

    @Query("SELECT i FROM Ingrediente i WHERE i.cantidad = ?1")
    List<Ingrediente> findByCantidad(@Positive Integer cantidad);

    @Query("SELECT i FROM Ingrediente i WHERE i.productoInventario.id = ?1")
    List<Ingrediente> findByProductoInventarioId(@Positive Integer productoInventario);

    @Query("SELECT i FROM Ingrediente i WHERE i.productoInventario.proveedor.negocio.id = ?1")
    List<Ingrediente> findByNegocioId(@Positive Integer proveedor);

    @Query("SELECT i FROM Ingrediente i WHERE i.productoInventario.proveedor.negocio.dueno.id = ?1")
    List<Ingrediente> findByDuenoId(@Positive Integer dueno);

    @Query("SELECT i FROM Ingrediente i WHERE i.productoVenta.id = ?1")
    List<Ingrediente> findByProductoVentaId(@Positive Integer productoVenta);

}
