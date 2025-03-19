package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProductoInventarioRepository extends CrudRepository<ProductoInventario,String>{

    public List<ProductoInventario> findAll();

    public ProductoInventario findById(Integer id);
    
    public ProductoInventario findByName(String name);
    
    public List<ProductoInventario> findByCategoriaInventario(CategoriasInventario categoriaInventario);
    
}
