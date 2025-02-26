package ispp_g2.gastrostock.Almacenes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/almacenes")
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    // Obtener todos los almacenes
    @GetMapping
    public List<Almacen> obtenerTodos() {
        return almacenService.obtenerTodos();
    }

    // Obtener almacén por ID
    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerPorId(@PathVariable Long id) {
        Optional<Almacen> almacen = almacenService.obtenerPorId(id);
        return almacen.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un almacén
    @PostMapping
    public ResponseEntity<Almacen> crearAlmacen(@RequestBody Almacen almacen) {
        try {
            Almacen nuevoAlmacen = almacenService.crearAlmacen(almacen);
            return ResponseEntity.ok(nuevoAlmacen);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un almacén
    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizarAlmacen(@PathVariable Long id, @RequestBody Almacen almacen) {
        try {
            Almacen almacenActualizado = almacenService.actualizarAlmacen(id, almacen);
            return ResponseEntity.ok(almacenActualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar almacén por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (almacenService.obtenerPorId(id).isPresent()) {
            almacenService.eliminar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar almacenes con más de X metros cuadrados
    @GetMapping("/buscar")
    public List<Almacen> buscarPorMetros(@RequestParam Double metros) {
        return almacenService.buscarPorMetrosCuadrados(metros);
    }
}

