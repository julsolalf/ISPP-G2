package ispp_g2.gastrostock.productoVenta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductoVentaRepository extends CrudRepository<ProductoVenta,String>{

    @Query("SELECT p FROM ProductoVenta p WHERE p.nombre = ?1")
    List<ProductoVenta> findProductoVentaByNombre(String nombre);

    @Query("SELECT p FROM ProductoVenta p WHERE p.categoriaVenta = ?1")
    List<ProductoVenta> findProductoVentaByCategoriaVenta(CategoriasVenta categoriaVenta);

    @Query("SELECT p FROM ProductoVenta p WHERE p.precioVenta = ?1")
    List<ProductoVenta> findProductoVentaByPrecioVenta(Double precioVenta);

    @Query("SELECT p FROM ProductoVenta p WHERE p.lineaDePedido.id = ?1")
    List<ProductoVenta> findProductoVentaByLineaDePedidoId(Integer lineaDePedido);

    @Query("SELECT p FROM ProductoVenta p WHERE p.nombre = ?1 AND p.precioVenta = ?2")
    List<ProductoVenta> findProductoVentaByNombreAndPrecioVenta(String nombre, Double precioVenta);

    @Query("SELECT p FROM ProductoVenta p WHERE p.categoriaVenta = ?1 AND p.precioVenta = ?2")
    List<ProductoVenta> findProductoVentaByCategoriaVentaAndPrecioVenta(CategoriasVenta categoriaVenta, Double precioVenta);

}
