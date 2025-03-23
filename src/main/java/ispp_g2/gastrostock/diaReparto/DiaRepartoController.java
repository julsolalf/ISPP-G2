package ispp_g2.gastrostock.diaReparto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/diasReparto")
public class DiaRepartoController {

    private DiaRepartoService diaRepartoService;

    @Autowired
    public DiaRepartoController(DiaRepartoService diaRepartoService) {
        this.diaRepartoService = diaRepartoService;
    }

    @GetMapping
    public ResponseEntity<List<DiaReparto>> findAll() {
        if(diaRepartoService.getDiasReparto().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(diaRepartoService.getDiasReparto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaReparto> findById(@PathVariable("id") String id) {
        DiaReparto diaReparto = diaRepartoService.getById(id);
        if(diaReparto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(diaReparto, HttpStatus.OK);
    }

    @GetMapping("/diaSemana/{diaSemana}")
    public ResponseEntity<List<DiaReparto>> findByDiaSemana(@PathVariable("diaSemana") String diaSemana) {
        List<DiaReparto> diasReparto = diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.valueOf(diaSemana));
        if(diasReparto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(diasReparto, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<DiaReparto>> findByNegocio(@PathVariable("negocio") String negocio) {
        List<DiaReparto> diasReparto = diaRepartoService.getDiaRepartoByNegocioId(negocio);
        if(diasReparto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(diasReparto, HttpStatus.OK);
    }

    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<DiaReparto>> findByProveedor(@PathVariable("proveedor") String proveedor) {
        List<DiaReparto> diasReparto = diaRepartoService.getDiaRepartoByProveedorId(proveedor);
        if(diasReparto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(diasReparto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiaReparto> save(@RequestBody @Valid DiaReparto diaReparto) {
        if(diaReparto == null) {
            throw  new IllegalArgumentException("Linea de pedido no puede ser nula");
        }
        return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaReparto> update(@PathVariable("id") String id, @RequestBody @Valid DiaReparto diaReparto) {
        if(diaReparto == null) {
            throw new IllegalArgumentException("Linea de pedido no puede ser nula");
        }
        if(diaRepartoService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        diaReparto.setId(Integer.valueOf(id));
        return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        DiaReparto diaReparto = diaRepartoService.getById(id);
        if(diaReparto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        diaRepartoService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
