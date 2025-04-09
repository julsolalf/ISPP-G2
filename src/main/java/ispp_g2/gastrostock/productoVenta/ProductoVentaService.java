package ispp_g2.gastrostock.productoVenta;

import ispp_g2.gastrostock.categorias.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProductoVentaService {

    private final ProductoVentaRepository productoVentaRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public ProductoVentaService(ProductoVentaRepository productoVentaRepository, CategoriaRepository categoriaRepository) {
        this.productoVentaRepository = productoVentaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public ProductoVenta getById(Integer id) {
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
    public List<ProductoVenta> getProductosVentaByNegocioID(Integer negocioId) {
        return productoVentaRepository.findProductoVentaByNegocioID(negocioId);
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVentaByDuenoID(Integer duenoId) {
        return productoVentaRepository.findProductoVentaByDuenoID(duenoId);
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
    public void delete(Integer id) {
        productoVentaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductoVenta convertirDTOProductoVenta(ProductoVentaDTO productoVentaDTO) {
        ProductoVenta productoVenta = new ProductoVenta();
        productoVenta.setName(productoVentaDTO.getName());
        productoVenta.setPrecioVenta(productoVentaDTO.getPrecioVenta());
        productoVenta.setCategoria(categoriaRepository.findById(productoVentaDTO.getCategoriaId()).orElse(null));
        return productoVenta;
    }

    @Transactional(readOnly = true)
    public ProductoVentaDTO convertirProductoVentaDTO(ProductoVenta productoVenta) {
        ProductoVentaDTO productoVentaDTO = new ProductoVentaDTO();
        productoVentaDTO.setName(productoVenta.getName());
        productoVentaDTO.setPrecioVenta(productoVenta.getPrecioVenta());
        productoVentaDTO.setCategoriaId(productoVenta.getCategoria().getId());
        return productoVentaDTO;
    }
    
}
