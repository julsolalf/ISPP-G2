package ispp_g2.gastrostock.lineaDeCarrito;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lineasDeCarrito")
public class LineaDeCarritoController {

    private final LineaDeCarritoService lineaDeCarritoService;

    @Autowired
    public LineaDeCarritoController(LineaDeCarritoService lineaDeCarritoService) {
        this.lineaDeCarritoService = lineaDeCarritoService;
    }

    @GetMapping
    public ResponseEntity<List<LineaDeCarrito>> findAll() {
        if (lineaDeCarritoService.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lineaDeCarritoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaDeCarrito> findById(@PathVariable("id") Integer id) {
        LineaDeCarrito lineaDeCarrito = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (lineaDeCarrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineaDeCarrito, HttpStatus.OK);
    }

    @GetMapping("/carrito/{id}")
    public ResponseEntity<List<LineaDeCarrito>> findByCarritoId(@PathVariable("id") Integer id) {
        List<LineaDeCarrito> lineasDeCarrito = lineaDeCarritoService.findLineaDeCarritoByCarritoId(id);
        if (lineasDeCarrito.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDeCarrito, HttpStatus.OK);
    }

    @GetMapping("/carrito/{idCarrito}/producto/{idProducto}")
    public ResponseEntity<List<LineaDeCarrito>> findByCarritoIdAndProductoId(@PathVariable("idCarrito") Integer idCarrito, @PathVariable("idProducto") Integer idProducto) {
        List<LineaDeCarrito> lineaDeCarrito = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(idCarrito, idProducto);
        if (lineaDeCarrito.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineaDeCarrito, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LineaDeCarrito> save(@RequestBody @Valid LineaDeCarrito lineaDeCarrito) {
        if (lineaDeCarrito == null) {
            throw new IllegalArgumentException("La línea de carrito no puede ser nula");
        }
        return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaDeCarrito> update(@PathVariable("id") Integer id, @RequestBody @Valid LineaDeCarrito lineaDeCarrito) {
        if (lineaDeCarrito == null) {
            throw new IllegalArgumentException("La línea de carrito no puede ser nula");
        }
        LineaDeCarrito lineaDeCarritoToUpdate = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (lineaDeCarritoToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lineaDeCarrito.setId(id);
        return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        LineaDeCarrito lineaDeCarrito = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (lineaDeCarrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lineaDeCarritoService.delete(lineaDeCarrito);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
