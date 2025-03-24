package ispp_g2.gastrostock.reabastecimiento;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;

@RestController
@RequestMapping("/api/reabastecimientos")
public class ReabastecimientoController {

    private final ReabastecimientoService reabastecimientoService;

    @Autowired
    public ReabastecimientoController(ReabastecimientoService reabastecimientoService) {
        this.reabastecimientoService = reabastecimientoService;
    }

    @GetMapping
    public ResponseEntity<List<Reabastecimiento>> findAll() {
        if(reabastecimientoService.getAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reabastecimientoService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reabastecimiento> findById(@PathVariable("id") String id) {
        Reabastecimiento reabastecimiento = reabastecimientoService.getById(id);
        if(reabastecimiento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimiento, HttpStatus.OK);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Reabastecimiento>> findByFecha(@PathVariable("fecha") LocalDate fecha) {
        List<Reabastecimiento> reabastecimientos = reabastecimientoService.getByFecha(fecha);
        if(reabastecimientos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/precioTotal/{precioTotal}")
    public ResponseEntity<List<Reabastecimiento>> findByPrecioTotal(@PathVariable("precioTotal") Double precioTotal) {
        List<Reabastecimiento> reabastecimientos = reabastecimientoService.getByPrecioTotal(precioTotal);
        if(reabastecimientos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<List<Reabastecimiento>> findByReferencia(@PathVariable("referencia") String referencia) {
        List<Reabastecimiento> reabastecimientos = reabastecimientoService.getByReferencia(referencia);
        if(reabastecimientos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Reabastecimiento>> findByNegocio(@PathVariable("negocio") String negocio) {
        List<Reabastecimiento> reabastecimientos = reabastecimientoService.getByNegocio(negocio);
        if(reabastecimientos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<Reabastecimiento>> findByProveedor(@PathVariable("proveedor") String proveedor) {
        List<Reabastecimiento> reabastecimientos = reabastecimientoService.getByProveedor(proveedor);
        if(reabastecimientos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/fecha/{fechaInicio}/{fechaFin}")
    public ResponseEntity<List<Reabastecimiento>> findByFechaBetween(@PathVariable("fechaInicio") LocalDate fechaInicio, @PathVariable("fechaFin") LocalDate fechaFin) {
        List<Reabastecimiento> reabastecimientos = reabastecimientoService.getByFechaBetween(fechaInicio, fechaFin);
        if(reabastecimientos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reabastecimiento> save(@RequestBody @Valid Reabastecimiento reabastecimiento) {
        if(reabastecimiento == null) {
            throw new IllegalArgumentException("Reabastecimiento no puede ser nulo");
        }
        return new ResponseEntity<>(reabastecimientoService.save(reabastecimiento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reabastecimiento> update(@PathVariable("id") String id, @RequestBody @Valid Reabastecimiento reabastecimiento) {
        if(reabastecimiento == null) {
            throw new IllegalArgumentException("Reabastecimiento no puede ser nulo");
        }
        if(reabastecimiento.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        reabastecimiento.setId(Integer.valueOf(id));
        return new ResponseEntity<>(reabastecimientoService.save(reabastecimiento), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        Reabastecimiento reabastecimiento = reabastecimientoService.getById(id);
        if(reabastecimiento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        reabastecimientoService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
