package ispp_g2.gastrostock.proveedores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<Proveedor> getAll() {
        return proveedorService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Proveedor> getById(@PathVariable Integer id) {
        return proveedorService.getById(id);
    }

    @GetMapping("/nombre")
    public List<Proveedor> getByFirstName(@RequestParam String firstName) {
        return proveedorService.getByFirstName(firstName);
    }

    @GetMapping("/dia-reparto/{dia}")
    public List<Proveedor> getByDiaReparto(@PathVariable DayOfWeek dia) {
        return proveedorService.getByDiaReparto(dia);
    }

    @PostMapping
    public Proveedor create(@RequestBody Proveedor proveedor) {
        return proveedorService.save(proveedor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        proveedorService.deleteById(id);
    }
}
