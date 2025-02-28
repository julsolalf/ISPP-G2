package ispp_g2.gastrostock.Ventas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<Venta> agregarVenta(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.agregarVenta(venta);
            return ResponseEntity.ok(nuevaVenta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaService.getVentas();
        return ResponseEntity.ok(ventas);
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Integer id) {
        Optional<Venta> venta = ventaService.obtenerVentaPorId(id);
        return venta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Agregar un producto específico a una venta existente
    @PostMapping("/{ventaId}/productos/{productoId}")
    public ResponseEntity<Void> agregarProductoAVenta(@PathVariable Integer ventaId, @PathVariable Integer productoId, @RequestParam Integer cantidad) {
        try {
            ventaService.agregarProductoAVenta(ventaId, productoId, cantidad);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar una venta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
        try {
            ventaService.eliminarVenta(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

