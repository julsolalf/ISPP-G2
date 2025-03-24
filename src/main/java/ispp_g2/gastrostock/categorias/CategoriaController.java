package ispp_g2.gastrostock.categorias;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll() {
        List<Categoria> categorias = categoriaService.getCategorias();
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> findById(@PathVariable("id") String id) {
        Categoria categoria = categoriaService.getById(id);
        if (categoria == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<List<Categoria>> findByNegocioId(@PathVariable("negocioId") String negocioId) {
        List<Categoria> categorias = categoriaService.getCategoriasByNegocioId(negocioId);
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/nombre/{name}")
    public ResponseEntity<List<Categoria>> findByName(@PathVariable("name") String name) {
        List<Categoria> categorias = categoriaService.getCategoriasByName(name);
        if (categorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Categoria> save(@Valid @RequestBody Categoria categoria) {
        if(categoria==null)
            throw new IllegalArgumentException(("Categoria no puede ser nula"));
        return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable("id") String id, @Valid @RequestBody Categoria categoria) {
        Categoria categoriaActual = categoriaService.getById(id);
        if (categoriaActual == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoria.setId(Integer.valueOf(id));
        return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Categoria categoria = categoriaService.getById(id);
        if (categoria == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoriaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
