package ispp_g2.gastrostock.ingrediente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    @Autowired
    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional(readOnly = true)
    public Ingrediente getById(String id) {
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

    @Transactional
    public Ingrediente save(Ingrediente ingrediente) {
        return ingredienteRepository.save(ingrediente);
    }

    @Transactional
    public void deleteById(String id) {
        ingredienteRepository.deleteById(id);
    }

}
