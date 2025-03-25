package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoInventarioRepository extends CrudRepository<ProductoInventario,String>{

    @Query("SELECT p FROM ProductoInventario p WHERE p.name = ?1")
    ProductoInventario findByName(String name);

    @Query("SELECT p FROM ProductoInventario p WHERE p.categoria.name = ?1")
    List<ProductoInventario> findByCategoriaName(String categoria);

    @Query("SELECT p FROM ProductoInventario p WHERE p.precioCompra = ?1")
    List<ProductoInventario> findByPrecioCompra(Double precioCompra);

    @Query("SELECT p FROM ProductoInventario p WHERE p.cantidadDeseada = ?1")
    List<ProductoInventario> findByCantidadDeseada(Integer cantidadDeseada);

    @Query("SELECT p FROM ProductoInventario p WHERE p.cantidadAviso = ?1")
    List<ProductoInventario> findByCantidadAviso(Integer cantidadAviso);

}
