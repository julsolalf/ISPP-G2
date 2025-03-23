package ispp_g2.gastrostock.dueño;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dueño")
public class DueñoController {

    private final DueñoService duenoService;

    @Autowired
    public DueñoController(DueñoService duenoService) {
        this.duenoService = duenoService;
    }

    @GetMapping
    public ResponseEntity<List<Dueño>> findAll() {
        if (duenoService.getAllDueños().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(duenoService.getAllDueños());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueño> findById(@PathVariable("id") String id) {
        Dueño dueno = duenoService.getDueñoById(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(duenoService.getDueñoById(id));
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Dueño> findByToken(@PathVariable("token") String token) {
        Dueño dueno = duenoService.getDueñoByToken(token);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(duenoService.getDueñoByToken(token));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Dueño> findByEmail(@PathVariable("email") String email) {
        Dueño dueno = duenoService.getDueñoByEmail(email);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(duenoService.getDueñoByEmail(email));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Dueño>> findByNombre(@PathVariable("nombre") String nombre) {
        List<Dueño> duenos = duenoService.getDueñoByNombre(nombre);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(duenoService.getDueñoByNombre(nombre));
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Dueño>> findByApellido(@PathVariable("apellido") String apellido) {
        List<Dueño> duenos = duenoService.getDueñoByApellido(apellido);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(duenoService.getDueñoByApellido(apellido));
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Dueño> findByTelefono(@PathVariable("telefono") String telefono) {
        Dueño dueno = duenoService.getDueñoByTelefono(telefono);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(duenoService.getDueñoByTelefono(telefono));
    }

    @GetMapping("/negocio/{id}")
    public ResponseEntity<Dueño> findByNegocio(@PathVariable("id") String id) {
        Dueño dueno = duenoService.getDueñoByNegocio(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(duenoService.getDueñoByNegocio(id));
    }

    @PostMapping
    public ResponseEntity<Dueño> save(@RequestBody @Valid Dueño dueño) {
        return ResponseEntity.ok(duenoService.saveDueño(dueño));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dueño> update(@PathVariable("id") String id, @RequestBody @Valid Dueño dueño) {
        if (dueño == null) {
            throw new IllegalArgumentException("Dueño no puede ser nulo");
        }
        if (duenoService.getDueñoById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dueño.setId(Integer.valueOf(id));
        return new ResponseEntity<>(duenoService.saveDueño(dueño), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Dueño dueño = duenoService.getDueñoById(id);
        if (dueño == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        duenoService.deleteDueño(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}