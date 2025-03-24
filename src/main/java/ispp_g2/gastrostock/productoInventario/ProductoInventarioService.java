package ispp_g2.gastrostock.productoInventario;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class ProductoInventarioService {
        
    private final ProductoInventarioRepository productoInventarioRepository;

    @Autowired
    public ProductoInventarioService(ProductoInventarioRepository ProductoInventarioRepository) {
        this.productoInventarioRepository = ProductoInventarioRepository;
    }

    @Transactional(readOnly = true)
    public ProductoInventario getById(String id) {
        return productoInventarioRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductosInventario() {
        Iterable<ProductoInventario> productoInventario = productoInventarioRepository.findAll();
        return StreamSupport.stream(productoInventario.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoInventario getByName(String name) {
        return productoInventarioRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByCategoriaName(String categoriaInventario) {
        return productoInventarioRepository.findByCategoriaName(categoriaInventario);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByPrecioCompra(Double precioCompra) {
        return productoInventarioRepository.findByPrecioCompra(precioCompra);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByCantidadDeseada(Integer cantidadDeseada) {
        return productoInventarioRepository.findByCantidadDeseada(cantidadDeseada);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByCantidadAviso(Integer cantidadAviso) {
        return productoInventarioRepository.findByCantidadAviso(cantidadAviso);
    }

    @Transactional
    public ProductoInventario save(@Valid ProductoInventario newProductoInventario){
        return productoInventarioRepository.save(newProductoInventario);
    }

    @Transactional
    public void delete(String id){
        productoInventarioRepository.deleteById(id);
    }
}
