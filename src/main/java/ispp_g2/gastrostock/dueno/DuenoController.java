package ispp_g2.gastrostock.dueno;

import java.util.ArrayList;
import java.util.List;

import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/duenos")
public class DuenoController {

    private final DuenoService duenoService;
    private final UserService userService;

    @Autowired
    public DuenoController(DuenoService duenoService, UserService userService) {
        this.duenoService = duenoService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Dueno>> findAll() {
        if (duenoService.getAllDuenos().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(duenoService.getAllDuenos(), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<DuenoDTO>> findAllDTO() {
        if (duenoService.getAllDuenos().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<DuenoDTO> denos = new ArrayList<>();
        for (Dueno d : duenoService.getAllDuenos()) {
            denos.add(duenoService.convertirDuenoDTO(d));
        }
        return new ResponseEntity<>(denos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueno> findById(@PathVariable("id") Integer id) {
        Dueno dueno = duenoService.getDuenoById(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<DuenoDTO> findDTOById(@PathVariable("id") Integer id) {
        DuenoDTO dueno = duenoService.convertirDuenoDTO(duenoService.getDuenoById(id));
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
    public ResponseEntity<Dueno> findByUser(@PathVariable("id") Integer id) {
        Dueno dueno = duenoService.getDuenoByUser(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dueno> save(@RequestBody @Valid DuenoDTO duenoDTO) {
        if(duenoDTO == null)
            throw new IllegalArgumentException("Dueno no puede ser nulo");
        Dueno dueno = duenoService.convertirDTODueno(duenoDTO);
        if(userService.findUserByUsername(duenoDTO.getUsername()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>(duenoService.saveDueno(dueno), HttpStatus.CREATED);
    }

    private boolean checkUsernameNonAvailable(String username, Integer id){
        User existingUser = userService.findUserByUsername(username);
        if(existingUser == null){
            return false;
        }
        Integer duenoToUpdate = existingUser.getId();
        Integer currDuenoWithUsername = duenoService.getDuenoById(id).getUser().getId();
        return !duenoToUpdate.equals(currDuenoWithUsername);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dueno> update(@PathVariable("id") Integer id, @RequestBody @Valid DuenoDTO duenoDTO) {
        // Check if the data is empty
        if (duenoDTO == null) {
            throw new IllegalArgumentException("Dueno no puede ser nulo");
        }
        // Check if the dueno exists
        if (duenoService.getDuenoById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Dueno dueno = duenoService.convertirDTODueno(duenoDTO);
        dueno.setId(id);
        // Check if the username is already in use
        if (checkUsernameNonAvailable(duenoDTO.getUsername(), id))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        // Save the user
        dueno.getUser().setId(duenoService.getDuenoById(id).getUser().getId());
        return new ResponseEntity<>(duenoService.saveDueno(dueno), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        Dueno dueno = duenoService.getDuenoById(id);
        if (dueno == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        duenoService.deleteDueno(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}