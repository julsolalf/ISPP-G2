package ispp_g2.gastrostock.productoInventario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.lote.LoteRepository;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class ProductoInventarioService {
        
    private final ProductoInventarioRepository productoInventarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final LoteRepository loteRepository;

    @Autowired
    public ProductoInventarioService(ProductoInventarioRepository ProductoInventarioRepository, CategoriaRepository categoriaRepository, ProveedorRepository proveedorRepository, LoteRepository loteRepository) {
        this.productoInventarioRepository = ProductoInventarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.loteRepository = loteRepository;
    }

    @Transactional(readOnly = true)
    public ProductoInventario getById(Integer id) {
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
    public List<ProductoInventario> getProductoInventarioByCategoriaId(Integer categoriaId) {
        return productoInventarioRepository.findByCategoriaId(categoriaId);
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

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByProveedorId(Integer proveedorId) {
        return productoInventarioRepository.findByProveedorId(proveedorId);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByNegocioId(Integer negocioId) {
        return productoInventarioRepository.findByNegocioId(negocioId);
    }

    @Transactional(readOnly = true)
    public List<ProductoInventario> getProductoInventarioByDuenoId(Integer duenoId) {
        return productoInventarioRepository.findByDuenoId(duenoId);
    }

    @Transactional(readOnly = true)
    public Map<ProductoInventario,Integer> getProductoInventarioMenosCantidad(Integer negocioId) {
        Map<ProductoInventario,Integer> productoInventarioMenosCantidad = new HashMap<>();
        List<ProductoInventario> productosInventario = productoInventarioRepository.findByNegocioId(negocioId);
        productosInventario.forEach(p -> {
            List<Lote> lotes = loteRepository.findByProductoId(p.getId());
            Integer cantidad = p.calcularCantidad(lotes);
            productoInventarioMenosCantidad.put(p, cantidad);
        });
        return productoInventarioMenosCantidad.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    @Transactional(readOnly = true)
    public Map<ProductoInventario,Integer> getProductoInventarioStockEmergencia(Integer negocioId) {
        Map<ProductoInventario,Integer> productoInventarioMenosCantidad = new HashMap<>();
        List<ProductoInventario> productosInventario = productoInventarioRepository.findByNegocioId(negocioId);
        productosInventario.forEach(p -> {
            List<Lote> lotes = loteRepository.findByProductoId(p.getId());
            Integer cantidad = p.calcularCantidad(lotes);
            if(p.getCantidadAviso()>=cantidad)
                productoInventarioMenosCantidad.put(p, cantidad);
        });
        return productoInventarioMenosCantidad.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    @Transactional
    public ProductoInventario save(@Valid ProductoInventario newProductoInventario){
        return productoInventarioRepository.save(newProductoInventario);
    }

    @Transactional
    public void delete(Integer id){
        productoInventarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductoInventarioDTO convertirProductoInventarioDTO(ProductoInventario productoInventario) {
        ProductoInventarioDTO productoInventarioDTO = new ProductoInventarioDTO();
        productoInventarioDTO.setName(productoInventario.getName());
        productoInventarioDTO.setPrecioCompra(productoInventario.getPrecioCompra());
        productoInventarioDTO.setCantidadDeseada(productoInventario.getCantidadDeseada());
        productoInventarioDTO.setCantidadAviso(productoInventario.getCantidadAviso());
        productoInventarioDTO.setCategoriaId(productoInventario.getCategoria().getId());
        productoInventarioDTO.setProveedorId(productoInventario.getProveedor().getId());
        return productoInventarioDTO;
    }

    @Transactional(readOnly = true)
    public ProductoInventario convertirDTOProductoInventario(ProductoInventarioDTO productoInventarioDTO) {
        ProductoInventario productoInventario = new ProductoInventario();
        productoInventario.setName(productoInventarioDTO.getName());
        productoInventario.setPrecioCompra(productoInventarioDTO.getPrecioCompra());
        productoInventario.setCantidadDeseada(productoInventarioDTO.getCantidadDeseada());
        productoInventario.setCantidadAviso(productoInventarioDTO.getCantidadAviso());
        productoInventario.setCategoria(categoriaRepository.findById(productoInventarioDTO.getCategoriaId()).orElse(null));
        productoInventario.setProveedor(proveedorRepository.findById(productoInventarioDTO.getProveedorId()).orElse(null));
        return productoInventario;
    }
}
