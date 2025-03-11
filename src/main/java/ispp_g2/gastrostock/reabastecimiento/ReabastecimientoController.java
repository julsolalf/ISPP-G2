package ispp_g2.gastrostock.reabastecimiento;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reabastecimientos")
public class ReabastecimientoController {

    private final ReabastecimientoService reabastecimientoService;
    private final ProveedorService proveedorService;

    @Autowired
    public ReabastecimientoController(ReabastecimientoService reabastecimientoService, ProveedorService proveedorService) {
        this.reabastecimientoService = reabastecimientoService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public Iterable<Reabastecimiento> getAll() {
        return reabastecimientoService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Reabastecimiento> getById(@PathVariable Integer id) {
        return reabastecimientoService.getById(id);
    }

    @GetMapping("/referencia/{referencia}")
    public Optional<Reabastecimiento> getByReferencia(@PathVariable String referencia) {
        return reabastecimientoService.getByReferencia(referencia);
    }

    // Endpoint para filtrar por fechas
    @GetMapping("/fechas")
    public List<Reabastecimiento> getByFechaBetween(
        @RequestParam("fechaInicio") String fechaInicio, 
        @RequestParam("fechaFin") String fechaFin) {
        
        LocalDate fechaInicioParsed = LocalDate.parse(fechaInicio);
        LocalDate fechaFinParsed = LocalDate.parse(fechaFin);
        
        return reabastecimientoService.getByFechaBetween(fechaInicioParsed, fechaFinParsed);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public List<Reabastecimiento> getByProveedor(@PathVariable Integer proveedorId) {
        Optional<Proveedor> proveedor = proveedorService.getById(proveedorId);
        if (proveedor.isPresent()) {
            return reabastecimientoService.getByProveedor(proveedor.get()); 
        }
        return List.of(); 
    }

    @PostMapping
    public Reabastecimiento create(@RequestBody Reabastecimiento reabastecimiento) {
        return reabastecimientoService.save(reabastecimiento);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        reabastecimientoService.deleteById(id);
    }
}
