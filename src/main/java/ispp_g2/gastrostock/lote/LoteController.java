package ispp_g2.gastrostock.lote;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lote")
public class LoteController {

    private LoteService loteService;

    @Autowired
    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping
    public ResponseEntity<List<Lote>> findAll() {
        if(loteService.getLotes().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(loteService.getLotes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> findById(@PathVariable("id") String id) {
        Lote lote = loteService.getById(id);
        if(lote == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lote, HttpStatus.OK);
    }

    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<Lote>> findByCantidad(@PathVariable("cantidad") Integer cantidad) {
        List<Lote> lotes = loteService.getLotesByCantidad(cantidad);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/fechaCaducidad/{fechaCaducidad}")
    public ResponseEntity<List<Lote>> findByFechaCaducidad(@PathVariable("fechaCaducidad") LocalDate fechaCaducidad) {
        List<Lote> lotes = loteService.getLotesByFechaCaducidad(fechaCaducidad);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/producto/{producto}")
    public ResponseEntity<List<Lote>> findByProductoId(@PathVariable("producto") Integer producto) {
        List<Lote> lotes = loteService.getLotesByProductoId(producto);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/reabastecimiento/{reabastecimiento}")
    public ResponseEntity<List<Lote>> findByReabastecimientoId(@PathVariable("reabastecimiento") Integer reabastecimiento) {
        List<Lote> lotes = loteService.getLotesByReabastecimientoId(reabastecimiento);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Lote> save(@RequestBody @Valid Lote lote) {
        if (lote == null){
            throw new IllegalArgumentException("El lote no puede ser nulo");
        }
        return new ResponseEntity<>(loteService.save(lote), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lote> update(@PathVariable("id") String id, @RequestBody @Valid Lote lote) {
        if (lote == null) {
            throw new IllegalArgumentException("El lote no puede ser nulo");
        }
        if (loteService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lote.setId(Integer.valueOf(id));
        return new ResponseEntity<>(loteService.save(lote), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if (loteService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loteService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
