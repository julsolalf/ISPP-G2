package ispp_g2.gastrostock.carrito;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    @Autowired
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<List<Carrito>> findAll() {
        if(carritoService.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carritoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> findById(@PathVariable("id") Integer id) {
        Carrito carrito = carritoService.findById(id);
        if(carrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<Carrito>> findByProveedor(@PathVariable("proveedor") Integer proveedor) {
        List<Carrito> carritos = carritoService.findByProveedorId(proveedor);
        if(carritos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carritos, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Carrito>> findByNegocio(@PathVariable("negocio") Integer negocio) {
        List<Carrito> carritos = carritoService.findByNegocioId(negocio);
        if(carritos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carritos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Carrito> save(@RequestBody @Valid Carrito carrito) {
        if(carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo");
        }
        return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrito> update(@PathVariable("id") Integer id, @RequestBody @Valid Carrito carrito) {
        if(carrito == null) {
            throw new IllegalArgumentException("El carrito no puede ser nulo");
        }
        Carrito carritoToUpdate = carritoService.findById(id);
        if(carritoToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        carrito.setId(id);
        return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        Carrito carrito = carritoService.findById(id);
        if(carrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        carritoService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
