package ispp_g2.gastrostock.Negocios;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {

    private final NegocioService negocioService;

    @Autowired
    public NegocioController(NegocioService negocioService) {
        this.negocioService = negocioService;
    }

    @GetMapping
    public List<Negocio> getAllNegocios() {
        return negocioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Negocio> getNegocioById(@PathVariable Long id) {
        Optional<Negocio> negocio = negocioService.findById(id);
        return negocio.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cif/{cifNif}")
    public ResponseEntity<Negocio> getNegocioByCif(@PathVariable String cifNif) {
        Optional<Negocio> negocio = negocioService.findByCifNif(cifNif);
        return negocio.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Negocio> createNegocio(@RequestBody Negocio negocio) {
        Negocio savedNegocio = negocioService.save(negocio);
        return ResponseEntity.ok(savedNegocio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNegocio(@PathVariable Long id) {
        if (negocioService.findById(id).isPresent()) {
            negocioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
