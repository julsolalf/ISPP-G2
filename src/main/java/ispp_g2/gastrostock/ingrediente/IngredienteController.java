package ispp_g2.gastrostock.ingrediente;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    @Autowired
    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping
    public ResponseEntity<List<Ingrediente>> findAll() {
        if (ingredienteService.getIngredientes().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ingredienteService.getIngredientes(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> findById(@PathVariable("id") Integer id) {
        try {
            Ingrediente ingrediente = ingredienteService.getById(id);
            if (ingrediente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(ingrediente, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<Ingrediente>> findByCantidad(@PathVariable("cantidad") Integer cantidad) {
        List<Ingrediente> ingredientes = ingredienteService.getIngredientesByCantidad(cantidad);
        if (ingredientes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @GetMapping("/productoInventario/{productoInventario}")
    public ResponseEntity<List<Ingrediente>> findByProductoInventarioId(@PathVariable("productoInventario") Integer productoInventario) {
        List<Ingrediente> ingredientes = ingredienteService.getIngredientesByProductoInventarioId(productoInventario);
        if (ingredientes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @GetMapping("/productoVenta/{productoVenta}")
    public ResponseEntity<List<Ingrediente>> findByProductoVentaId(@PathVariable("productoVenta") Integer productoVenta) {
        List<Ingrediente> ingredientes = ingredienteService.getIngredientesByProductoVentaId(productoVenta);
        if (ingredientes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ingrediente> save(@RequestBody @Valid Ingrediente ingrediente) {
        if(ingrediente == null){
            throw new IllegalArgumentException("Ingrediente should not be null");
        }
        return new ResponseEntity<>(ingredienteService.save(ingrediente), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingrediente> update(@PathVariable("id") Integer id, @RequestBody @Valid Ingrediente ingrediente) {
        try {
            Ingrediente existingIngrediente = ingredienteService.getById(id);
            
            if (existingIngrediente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            ingrediente.setId(id);
            return new ResponseEntity<>(ingredienteService.save(ingrediente), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        try {
            Ingrediente ingrediente = ingredienteService.getById(id);
            if (ingrediente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            ingredienteService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
