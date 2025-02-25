package ispp_g2.gastrostock.Productos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository pr;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.pr = productoRepository;
    }

    // Crear un producto
    @Transactional
    public Producto crearProducto(Producto producto) {
        return pr.save(producto);
    }

    // Modificar un producto
    public Producto modificarProducto(Producto producto) {
        if (producto.getId() == null || !pr.existsById(producto.getId())) {
            throw new IllegalArgumentException("No se puede modificar un producto que no existe.");
        }
        return pr.save(producto);
    }

    // Eliminar un producto por id
    @Transactional
    public void eliminarProducto(Integer id) {
        Producto producto = obtenerProductoPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        pr.delete(producto);
    }

    // Eliminar un producto por nombre
    @Transactional
    public void eliminarProducto(String nombre) {
        Producto producto = obtenerProductoPorNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        pr.delete(producto);
    }

    // Consultar un producto por id
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Integer id) {
        return pr.findById(id);
    }

    // Consultar un producto por nombre
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorNombre(String nombre) {
        return pr.encontrarProductoPorNombre(nombre);
    }

    // Listar todos los productos
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return (List<Producto>) pr.findAll();
    }

    // Listar productos en riesgo de desperdicio
    @Transactional(readOnly = true)
    public List<Producto> listarProductosEnRiesgo(Integer diasAntes) {
        LocalDate fechaLimite = LocalDate.now().plusDays(diasAntes);
        return pr.encontrarProductosEnRiesgos(fechaLimite);
    }

    // Listar productos desperdiciados (caducados y marcados como desperdiciados)
    @Transactional(readOnly = true)
    public List<Producto> listarProductosDesperdiciados() {
        return pr.encontrarProductosDesperdiciados();
    }
}
