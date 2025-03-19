package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class ProductoInventarioService {
        
    private ProductoInventarioRepository pr;

    @Autowired
    public ProductoInventarioService(ProductoInventarioRepository ProductoInventarioRepository) {
        this.pr = ProductoInventarioRepository;
    }

    @Transactional(readOnly = true)
    public ProductoInventario getById(Integer id) {
        return pr.findById(id);
    }

    @Transactional(readOnly = true)
    public ProductoInventario getByName(String name) {
        return pr.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductosInventario() {
        return pr.findAll();
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByCategoria(CategoriasInventario categoriaInventario) {
        return pr.findByCategoriaInventario(categoriaInventario);
    }


    @Transactional
    public ProductoInventario saveProductoInventario(@Valid ProductoInventario newProductoInventario){
        return pr.save(newProductoInventario);
    }

    @Transactional
    public void deleteProductoInventario(Integer id){
        ProductoInventario productoInventarioToDelete = pr.findById(id);
        pr.delete(productoInventarioToDelete);
    }
}
