package ispp_g2.gastrostock.dueño;

import java.util.ArrayList;
import java.util.List;

import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dueños")
public class DueñoController {

    private final DueñoService duenoService;
    private final UserService userService;

    @Autowired
    public DueñoController(DueñoService duenoService, UserService userService) {
        this.duenoService = duenoService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Dueño>> findAll() {
        if (duenoService.getAllDueños().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(duenoService.getAllDueños(), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<DueñoDTO>> findAllDTO() {
        if (duenoService.getAllDueños().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<DueñoDTO> denos = new ArrayList<>();
        for (Dueño d : duenoService.getAllDueños()) {
            denos.add(duenoService.convertirDueñoDTO(d));
        }
        return new ResponseEntity<>(denos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueño> findById(@PathVariable("id") Integer id) {
        Dueño dueno = duenoService.getDueñoById(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<DueñoDTO> findDTOById(@PathVariable("id") Integer id) {
        DueñoDTO dueno = duenoService.convertirDueñoDTO(duenoService.getDueñoById(id));
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
    public ResponseEntity<Dueño> findByUser(@PathVariable("id") Integer id) {
        Dueño dueno = duenoService.getDueñoByUser(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDueñoByUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dueño> save(@RequestBody @Valid DueñoDTO dueñoDTO) {
        if(dueñoDTO == null)
            throw new IllegalArgumentException("Dueño no puede ser nulo");
        Dueño dueño = duenoService.convertirDTODueño(dueñoDTO);
        if(userService.findUserByUsername(dueñoDTO.getUsername()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>(duenoService.saveDueño(dueño), HttpStatus.CREATED);
    }

    private boolean checkUsernameNonAvailable(String username, Integer id){
        if(userService.findUserByUsername(username) == null){
            return false;
        }
        Integer duenoToUpdate = userService.findUserByUsername(username).getId();
        Integer currDuenoWithUsername = duenoService.getDueñoById(id).getUser().getId();
        return !duenoToUpdate.equals(currDuenoWithUsername);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dueño> update(@PathVariable("id") Integer id, @RequestBody @Valid DueñoDTO dueñoDTO) {
        // Check if the data is empty
        if (dueñoDTO == null) {
            throw new IllegalArgumentException("Dueño no puede ser nulo");
        }
        // Check if the dueño exists
        if (duenoService.getDueñoById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Dueño dueño = duenoService.convertirDTODueño(dueñoDTO);
        dueño.setId(id);
        // Check if the username is already in use
        if (checkUsernameNonAvailable(dueñoDTO.getUsername(), id))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        // Save the user
        dueño.getUser().setId(duenoService.getDueñoById(id).getUser().getId());
        return new ResponseEntity<>(duenoService.saveDueño(dueño), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        Dueño dueño = duenoService.getDueñoById(id);
        if (dueño == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        duenoService.deleteDueño(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}