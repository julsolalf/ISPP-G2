package ispp_g2.gastrostock.ingrediente;

import ispp_g2.gastrostock.productoInventario.ProductoInventarioRepository;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final ProductoVentaRepository productoVentaRepository;
    private final ProductoInventarioRepository productoInventarioRepository;

    @Autowired
    public IngredienteService(IngredienteRepository ingredienteRepository, ProductoVentaRepository productoVentaRepository, ProductoInventarioRepository productoInventarioRepository) {
        this.ingredienteRepository = ingredienteRepository;
        this.productoVentaRepository = productoVentaRepository;
        this.productoInventarioRepository = productoInventarioRepository;
    }

    @Transactional(readOnly = true)
    public Ingrediente getById(Integer id) {
        return ingredienteRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> getIngredientes() {
        Iterable<Ingrediente> ingredientes = ingredienteRepository.findAll();
        return StreamSupport.stream(ingredientes.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> getIngredientesByCantidad(Integer cantidad) {
        return ingredienteRepository.findByCantidad(cantidad);
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> getIngredientesByProductoInventarioId(Integer productoInventario) {
        return ingredienteRepository.findByProductoInventarioId(productoInventario);
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> getIngredientesByProductoVentaId(Integer productoVenta) {
        return ingredienteRepository.findByProductoVentaId(productoVenta);
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> getIngredientesByNegocioId(Integer negocio) {
        return ingredienteRepository.findByNegocioId(negocio);
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> getIngredientesByDuenoId(Integer dueno) {
        return ingredienteRepository.findByDuenoId(dueno);
    }

    @Transactional
    public Ingrediente save(Ingrediente ingrediente) {
        return ingredienteRepository.save(ingrediente);
    }

    @Transactional
    public void deleteById(Integer id) {
        ingredienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public IngredienteDTO convertirIngredienteToDTO(Ingrediente ingrediente) {
        IngredienteDTO ingredienteDTO = new IngredienteDTO();
        ingredienteDTO.setCantidad(ingrediente.getCantidad());
        ingredienteDTO.setProductoInventarioId(ingrediente.getProductoInventario().getId());
        ingredienteDTO.setProductoVentaId(ingrediente.getProductoVenta().getId());
        return ingredienteDTO;
    }

    @Transactional(readOnly = true)
    public Ingrediente convertirDTOToIngrediente(IngredienteDTO ingredienteDTO) {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setCantidad(ingredienteDTO.getCantidad());
        ingrediente.setProductoVenta(productoVentaRepository.findById(ingredienteDTO.getProductoVentaId()).orElse(null));
        ingrediente.setProductoInventario(productoInventarioRepository.findById(ingredienteDTO.getProductoInventarioId()).orElse(null));
        return ingrediente;
    }

}
