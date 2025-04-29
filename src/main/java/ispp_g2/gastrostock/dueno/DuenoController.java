package ispp_g2.gastrostock.dueno;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ispp_g2.gastrostock.exceptions.BadRequestException;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/duenos")
public class DuenoController {

    private final DuenoService duenoService;
    private final UserService userService;
    private final PasswordEncoder encoder;


    private final String adminAuth = "admin";
    private final String duenoAuth = "dueno";

    @Autowired
    public DuenoController(DuenoService duenoService, UserService userService, PasswordEncoder encoder) {
        this.duenoService = duenoService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping
    public ResponseEntity<List<Dueno>> findAll() {
        User u = userService.findCurrentUser();
        if(!(u.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (duenoService.getAllDuenos().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(duenoService.getAllDuenos(), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<DuenoDTO>> findAllDTO() {
        User u = userService.findCurrentUser();
        if(!(u.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (duenoService.getAllDuenos().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<DuenoDTO> denos = new ArrayList<>();
        for (Dueno d : duenoService.getAllDuenos()) {
            denos.add(duenoService.convertirDuenoDTO(d));
        }
        return new ResponseEntity<>(denos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueno> findById(@PathVariable("id") Integer id ) {
        User user = userService.findCurrentUser();
        Dueno dueno = duenoService.getDuenoById(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!((user.getId().equals(dueno.getUser().getId())) || user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<DuenoDTO> findDTOById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Dueno duenoToGet = duenoService.getDuenoById(id);
        if(duenoToGet == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!((user.getId().equals(duenoToGet.getUser().getId())) || user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        DuenoDTO dueno = duenoService.convertirDuenoDTO(duenoToGet);

        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Dueno> findByToken(@PathVariable("token") String token) {
        User user = userService.findCurrentUser();
        if(!(user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByToken(token);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByToken(token), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Dueno> findByEmail(@PathVariable("email") String email) {
        User user = userService.findCurrentUser();
        if(!(user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByEmail(email);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Dueno>> findByNombre(@PathVariable("nombre") String nombre) {
        User user = userService.findCurrentUser();
        if(!(user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Dueno> duenos = duenoService.getDuenoByNombre(nombre);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenos, HttpStatus.OK);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Dueno>> findByApellido(@PathVariable("apellido") String apellido) {
        User user = userService.findCurrentUser();
        if(!(user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Dueno> duenos = duenoService.getDuenoByApellido(apellido);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Dueno> findByTelefono(@PathVariable("telefono") String telefono) {
        User user = userService.findCurrentUser();
        if(!(user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByTelefono(telefono);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByTelefono(telefono), HttpStatus.OK);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<Dueno> findByUser(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(!((user.getAuthority().getAuthority().equals(adminAuth)) || user.getId().equals(id))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByUser(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dueno> save(@RequestBody @Valid DuenoDTO duenoDTO) {
        User user = userService.findCurrentUser();
        if(!(user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(duenoDTO == null)
            throw new IllegalArgumentException("Dueno no puede ser nulo");
        Dueno dueno = duenoService.convertirDTODueno(duenoDTO);
        if(userService.findUserByUsername(duenoDTO.getUsername()) != null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>(duenoService.saveDueno(dueno), HttpStatus.CREATED);
    }

    private boolean validarPassword(String password) {
        Pattern pattern = Pattern.compile(
            "^(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*\\d)" +
            "(?=.*[#$@!%&?¡\"+,.:;='^|~_()¿{}\\[\\]\\\\-])" +
            ".{8,32}$"
        );
        return pattern.matcher(password).matches();
    }

    private boolean validarTelefono(String telefono) {
        Pattern pattern = Pattern.compile("^[6789]\\d{8}$");
        return pattern.matcher(telefono).matches();
    }


    private boolean checkUsernameNonAvailable(String username, Integer id){
        User existingUser = userService.findUserByUsernameNull(username);
        if(existingUser == null){
            return false;
        }
        Integer duenoToUpdate = existingUser.getId();
        Integer currDuenoWithUsername = duenoService.getDuenoById(id).getUser().getId();
        return !duenoToUpdate.equals(currDuenoWithUsername);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dueno> update(@PathVariable("id") Integer id, @RequestBody @Valid DuenoDTO duenoDTO) {
        User user = userService.findCurrentUser();
        if(duenoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");

        if(!validarPassword(duenoDTO.getPassword())) {
            throw new BadRequestException("La contraseña debe tener entre 8 y 32 caracteres, 1 mayúscula, " +
            "1 minúscula, un número y un caracter especial");
        }
        if(!validarTelefono(duenoDTO.getNumTelefono())) {
            throw new BadRequestException("El teléfono debe ser correcto");
        }
        if(checkUsernameNonAvailable(duenoDTO.getUsername(), id)){
            throw new BadRequestException("El nombre de usuario ya está en uso"); 
        }

        Dueno toUpdate = duenoService.getDuenoById(id);
        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
                (user.getAuthority().getAuthority().equals(duenoAuth) && user.getId().equals(toUpdate.getUser().getId())))){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User newUser = new User();
        newUser.setUsername(duenoDTO.getUsername());
        newUser.setPassword(encoder.encode(duenoDTO.getPassword()));
        userService.updateUser(toUpdate.getUser().getId(), newUser);
        Dueno newDueno = new Dueno();
        newDueno.setFirstName(duenoDTO.getFirstName());
        newDueno.setLastName(duenoDTO.getLastName());
        newDueno.setEmail(duenoDTO.getEmail());
        newDueno.setNumTelefono(duenoDTO.getNumTelefono());
        duenoService.updateDueno(id, newDueno);

        return new ResponseEntity<>(duenoService.getDuenoById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Dueno dueno = duenoService.getDuenoById(id);
        if (dueno == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!(user.getAuthority().getAuthority().equals(adminAuth) || user.getId().equals(dueno.getUser().getId()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        duenoService.deleteDueno(id);
        userService.deleteUser(dueno.getUser().getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}