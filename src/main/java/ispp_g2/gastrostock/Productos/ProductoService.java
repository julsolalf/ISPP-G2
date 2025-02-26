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

    @Transactional
    public Producto crearProducto(Producto producto) {
        if (producto.getId() != null) {
            throw new IllegalArgumentException("Un producto nuevo no debe tener ID asignado.");
        }
        return pr.save(producto);
    }

    // Modificar un producto existente
    @Transactional
    public Producto modificarProducto(Producto producto) {
        if (producto.getId() == null || !pr.existsById(producto.getId())) {
            throw new IllegalArgumentException("No se puede modificar un producto que no existe.");
        }
        // Opcionalmente: obtener el producto actual, actualizar solo ciertos campos,
        // etc.
        // Por ejemplo:
        Producto productoExistente = pr.findById(producto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setFechaCaducidad(producto.getFechaCaducidad());
        productoExistente.setCantidad(producto.getCantidad());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setMedida(producto.getMedida());
        productoExistente.setTamaño(producto.getTamaño());
        productoExistente.setProveedor(producto.getProveedor());

        return pr.save(productoExistente);
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
