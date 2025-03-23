package ispp_g2.gastrostock.dueño;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dueños")
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
        return new ResponseEntity<>(duenoService.getAllDueños(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueño> findById(@PathVariable("id") String id) {
        Dueño dueno = duenoService.getDueñoById(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Dueño> findByToken(@PathVariable("token") String token) {
        Dueño dueno = duenoService.getDueñoByToken(token);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDueñoByToken(token), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Dueño> findByEmail(@PathVariable("email") String email) {
        Dueño dueno = duenoService.getDueñoByEmail(email);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDueñoByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Dueño>> findByNombre(@PathVariable("nombre") String nombre) {
        List<Dueño> duenos = duenoService.getDueñoByNombre(nombre);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenos, HttpStatus.OK);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Dueño>> findByApellido(@PathVariable("apellido") String apellido) {
        List<Dueño> duenos = duenoService.getDueñoByApellido(apellido);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDueñoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Dueño> findByTelefono(@PathVariable("telefono") String telefono) {
        Dueño dueno = duenoService.getDueñoByTelefono(telefono);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDueñoByTelefono(telefono), HttpStatus.OK);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<Dueño> findByUser(@PathVariable("id") String id) {
        Dueño dueno = duenoService.getDueñoByUser(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDueñoByUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dueño> save(@RequestBody @Valid Dueño dueño) {
        if(dueño == null)
            throw new IllegalArgumentException("Dueño no puede ser nulo");
        return new ResponseEntity<>(duenoService.saveDueño(dueño), HttpStatus.CREATED);
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