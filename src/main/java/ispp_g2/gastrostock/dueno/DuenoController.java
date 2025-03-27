package ispp_g2.gastrostock.dueno;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/duenos")
public class DuenoController {

    private final DuenoService duenoService;

    @Autowired
    public DuenoController(DuenoService duenoService) {
        this.duenoService = duenoService;
    }

    @GetMapping
    public ResponseEntity<List<Dueno>> findAll() {
        if (duenoService.getAllDuenos().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(duenoService.getAllDuenos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueno> findById(@PathVariable("id") String id) {
        Dueno dueno = duenoService.getDuenoById(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Dueno> findByToken(@PathVariable("token") String token) {
        Dueno dueno = duenoService.getDuenoByToken(token);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByToken(token), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Dueno> findByEmail(@PathVariable("email") String email) {
        Dueno dueno = duenoService.getDuenoByEmail(email);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Dueno>> findByNombre(@PathVariable("nombre") String nombre) {
        List<Dueno> duenos = duenoService.getDuenoByNombre(nombre);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenos, HttpStatus.OK);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Dueno>> findByApellido(@PathVariable("apellido") String apellido) {
        List<Dueno> duenos = duenoService.getDuenoByApellido(apellido);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Dueno> findByTelefono(@PathVariable("telefono") String telefono) {
        Dueno dueno = duenoService.getDuenoByTelefono(telefono);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByTelefono(telefono), HttpStatus.OK);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<Dueno> findByUser(@PathVariable("id") String id) {
        Dueno dueno = duenoService.getDuenoByUser(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dueno> save(@RequestBody @Valid Dueno dueno) {
        if(dueno == null)
            throw new IllegalArgumentException("Dueno no puede ser nulo");
        return new ResponseEntity<>(duenoService.saveDueno(dueno), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dueno> update(@PathVariable("id") String id, @RequestBody @Valid Dueno dueno) {
        if (dueno == null) {
            throw new IllegalArgumentException("Dueno no puede ser nulo");
        }
        if (duenoService.getDuenoById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dueno.setId(Integer.valueOf(id));
        return new ResponseEntity<>(duenoService.saveDueno(dueno), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Dueno dueno = duenoService.getDuenoById(id);
        if (dueno == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        duenoService.deleteDueno(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}