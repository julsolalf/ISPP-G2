package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoInventarioRepository extends CrudRepository<ProductoInventario,String>{

    @Query("SELECT p FROM ProductoInventario p WHERE p.name = ?1")
    public ProductoInventario findByName(String name);

    @Query("SELECT p FROM ProductoInventario p WHERE p.categoriaInventario = ?1")
    public List<ProductoInventario> findByCategoriaInventario(CategoriasInventario categoriaInventario);

    @Query("SELECT p FROM ProductoInventario p WHERE p.precioCompra = ?1")
    public List<ProductoInventario> findByPrecioCompra(Double precioCompra);

    @Query("SELECT p FROM ProductoInventario p WHERE p.cantidadDeseada = ?1")
    public List<ProductoInventario> findByCantidadDeseada(Integer cantidadDeseada);

    @Query("SELECT p FROM ProductoInventario p WHERE p.cantidadAviso = ?1")
    public List<ProductoInventario> findByCantidadAviso(Integer cantidadAviso);

}
