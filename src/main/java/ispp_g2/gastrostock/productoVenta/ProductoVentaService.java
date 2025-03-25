package ispp_g2.gastrostock.productoVenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProductoVentaService {

    private final ProductoVentaRepository productoVentaRepository;

    @Autowired
    public ProductoVentaService(ProductoVentaRepository productoVentaRepository) {
        this.productoVentaRepository = productoVentaRepository;
    }

    @Transactional(readOnly = true)
    public ProductoVenta getById(String id) {
        return productoVentaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVenta() {
        Iterable<ProductoVenta> productosVenta = productoVentaRepository.findAll();
        return StreamSupport.stream(productosVenta.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVentaByNombre(String nombre) {
        return productoVentaRepository.findProductoVentaByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVentaByCategoriaVenta(String categoriaVenta) {
        return productoVentaRepository.findProductoVentaByCategoriaVenta(categoriaVenta);
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVentaByPrecioVenta(Double precioVenta) {
        return productoVentaRepository.findProductoVentaByPrecioVenta(precioVenta);
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVentaByCategoriaVentaAndPrecioVenta(String categoriaVenta, Double precioVenta) {
        return productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta(categoriaVenta, precioVenta);
    }

    @Transactional
    public ProductoVenta save(ProductoVenta productoVenta) {
        return productoVentaRepository.save(productoVenta);
    }

    @Transactional
    public void delete(String id) {
        productoVentaRepository.deleteById(id);
    }
    
}
