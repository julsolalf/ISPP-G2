package ispp_g2.gastrostock.Productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    
    private final ProductoService ps;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.ps = productoService;
    }

    // Crear un producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = ps.crearProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    // Modificar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> modificarProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = ps.obtenerProductoPorId(id);
        if (productoExistente.isPresent()) {
            producto.setId(id);
            Producto productoActualizado = ps.modificarProducto(producto);
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un producto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        Optional<Producto> producto = ps.obtenerProductoPorId(id);
        if (producto.isPresent()) {
            ps.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        return ps.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener un producto por nombre
    @GetMapping("/{nombre}")
    public ResponseEntity<Producto> obtenerProductoPorNombre(@PathVariable String nombre) {
        return ps.obtenerProductoPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = ps.listarProductos();
        return ResponseEntity.ok(productos);
    }

    // Listar productos en riesgo de desperdicio (ejemplo: vencen en menos de X días)
    @GetMapping("/enRiesgo/{diasAntes}")
    public ResponseEntity<List<Producto>> listarProductosEnRiesgo(@PathVariable Integer diasAntes) {
        List<Producto> productosEnRiesgo = ps.listarProductosEnRiesgo(diasAntes);
        return ResponseEntity.ok(productosEnRiesgo);
    }

    // Listar productos desperdiciados
    @GetMapping("/desperdiciados")
    public ResponseEntity<List<Producto>> listarProductosDesperdiciados() {
        List<Producto> productosDesperdiciados = ps.listarProductosDesperdiciados();
        return ResponseEntity.ok(productosDesperdiciados);
    }
}
