package ispp_g2.gastrostock.productoVenta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoVentaRepository extends CrudRepository<ProductoVenta,Integer>{

    @Query("SELECT p FROM ProductoVenta p WHERE p.name = ?1")
    List<ProductoVenta> findProductoVentaByNombre(String nombre);

    @Query("SELECT p FROM ProductoVenta p WHERE p.categoria.name = ?1")
    List<ProductoVenta> findProductoVentaByCategoriaVenta(String categoriaVenta);

    @Query("SELECT p FROM ProductoVenta p WHERE p.precioVenta = ?1")
    List<ProductoVenta> findProductoVentaByPrecioVenta(Double precioVenta);

    @Query("SELECT p FROM ProductoVenta p WHERE p.categoria.negocio.id = ?1")
    List<ProductoVenta> findProductoVentaByNegocioID(Integer negocioId);

    @Query("SELECT p FROM ProductoVenta p WHERE p.categoria.negocio.dueno.id = ?1")
    List<ProductoVenta> findProductoVentaByDuenoID(Integer duenoId);

    @Query("SELECT p FROM ProductoVenta p WHERE p.categoria.name = ?1 AND p.precioVenta = ?2")
    List<ProductoVenta> findProductoVentaByCategoriaVentaAndPrecioVenta(String categoriaVenta, Double precioVenta);

    @Query("SELECT p FROM ProductoVenta p WHERE p.name = ?1 AND p.categoria.negocio.id = ?2")
    Optional<ProductoVenta> findProductoVentaByNombreAndNegocioId(String nombre, Integer negocioId);

}
