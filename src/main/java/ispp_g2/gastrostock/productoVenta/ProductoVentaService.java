package ispp_g2.gastrostock.productoVenta;

import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedido;
import ispp_g2.gastrostock.lineaDePedido.LineaDePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductoVentaService {

    private final ProductoVentaRepository productoVentaRepository;
    private final CategoriaRepository categoriaRepository;
    private final LineaDePedidoRepository lineaDePedidoRepository;

    @Autowired
    public ProductoVentaService(ProductoVentaRepository productoVentaRepository, CategoriaRepository categoriaRepository, LineaDePedidoRepository lineaDePedidoRepository) {
        this.productoVentaRepository = productoVentaRepository;
        this.categoriaRepository = categoriaRepository;
        this.lineaDePedidoRepository = lineaDePedidoRepository;
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
    public List<ProductoVenta> getProductosVentaByCategoriaId(Integer categoriaId) {
        return productoVentaRepository.findProductoVentaByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVentaByPrecioVenta(Double precioVenta) {
        return productoVentaRepository.findProductoVentaByPrecioVenta(precioVenta);
    }

    @Transactional(readOnly = true)
    public List<ProductoVenta> getProductosVentaByCategoriaVentaAndPrecioVenta(String categoriaVenta, Double precioVenta) {
        return productoVentaRepository.findProductoVentaByCategoriaVentaAndPrecioVenta(categoriaVenta, precioVenta);
    }

    @Transactional(readOnly = true)
    public Map<ProductoVenta, Integer> getProductosVentaMasVendidosByNegocio(Integer negocioId) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoRepository.findLineaDePedidosByNegocioId(negocioId);
        Map<ProductoVenta, Integer> mostSelled = new HashMap<>();
        lineasDePedido.forEach(lineaDePedido -> {
            ProductoVenta productoVenta = lineaDePedido.getProducto();
            if(mostSelled.containsKey(productoVenta)){
                mostSelled.put(productoVenta, mostSelled.get(productoVenta) + lineaDePedido.getCantidad());
            } else {
                mostSelled.put(productoVenta, lineaDePedido.getCantidad());
            }
        });
        // Se devuelven los productos ordenados por cantidad vendida
        return  mostSelled.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1 ,e2) -> e1, LinkedHashMap::new
                ));
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
