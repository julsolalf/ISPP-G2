package ispp_g2.gastrostock.dueno;

import java.util.ArrayList;
import java.util.List;

import ispp_g2.gastrostock.config.jwt.JwtService;
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
    private final JwtService jwtService;

    private final String admin = "admin";

    @Autowired
    public DuenoController(DuenoService duenoService, UserService userService, JwtService jwtService) {
        this.duenoService = duenoService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    private User findUserByJWT(String authToken) {
        String token =authToken.substring("Bearer ".length());
        String username = jwtService.getUserNameFromJwtToken(token);
        return userService.findUserByUsername(username);
    }

    @GetMapping
    public ResponseEntity<List<Dueno>> findAll(@RequestHeader("Authorization") String authHeader) {
        User u = findUserByJWT(authHeader);
        if(!(u.getAuthority().getAuthority().equals(admin))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (duenoService.getAllDuenos().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(duenoService.getAllDuenos(), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<DuenoDTO>> findAllDTO(@RequestHeader("Authorization") String authHeader) {
        User u = findUserByJWT(authHeader);
        if(!(u.getAuthority().getAuthority().equals(admin))) {
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
    public ResponseEntity<Dueno> findById(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer id ) {
        User user = findUserByJWT(authHeader);
        Dueno dueno = duenoService.getDuenoById(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!((user.getId().equals(dueno.getUser().getId())) || user.getAuthority().getAuthority().equals(admin))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<DuenoDTO> findDTOById(@RequestHeader("Authorization") String authHeader,
                                                @PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
        Dueno duenoToGet = duenoService.getDuenoById(id);
        if(duenoToGet == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!((user.getId().equals(duenoToGet.getUser().getId())) || user.getAuthority().getAuthority().equals(admin))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        DuenoDTO dueno = duenoService.convertirDuenoDTO(duenoToGet);

        return new ResponseEntity<>(dueno, HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Dueno> findByToken(@RequestHeader("Authorization") String authHeader, @PathVariable("token") String token) {
        User user = findUserByJWT(authHeader);
        if(!(user.getAuthority().getAuthority().equals(admin))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByToken(token);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByToken(token), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Dueno> findByEmail(@RequestHeader("Authorization") String authHeader,@PathVariable("email") String email) {
        User user = findUserByJWT(authHeader);
        if(!(user.getAuthority().getAuthority().equals(admin))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByEmail(email);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Dueno>> findByNombre(@RequestHeader("Authorization") String authHeader,@PathVariable("nombre") String nombre) {
        User user = findUserByJWT(authHeader);
        if(!(user.getAuthority().getAuthority().equals(admin))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Dueno> duenos = duenoService.getDuenoByNombre(nombre);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenos, HttpStatus.OK);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Dueno>> findByApellido(@RequestHeader("Authorization") String authHeader,@PathVariable("apellido") String apellido) {
        User user = findUserByJWT(authHeader);
        if(!(user.getAuthority().getAuthority().equals(admin))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Dueno> duenos = duenoService.getDuenoByApellido(apellido);
        if(duenos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Dueno> findByTelefono(@RequestHeader("Authorization") String authHeader,@PathVariable("telefono") String telefono) {
        User user = findUserByJWT(authHeader);
        if(!(user.getAuthority().getAuthority().equals(admin))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByTelefono(telefono);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByTelefono(telefono), HttpStatus.OK);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<Dueno> findByUser(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
        if(!((user.getAuthority().getAuthority().equals(admin)) || user.getId().equals(id))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Dueno dueno = duenoService.getDuenoByUser(id);
        if(dueno == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(duenoService.getDuenoByUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dueno> save(@RequestHeader("Authorization") String authHeader,@RequestBody @Valid DuenoDTO duenoDTO) {
        User user = findUserByJWT(authHeader);
        if(!(user.getAuthority().getAuthority().equals(admin))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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
    public ResponseEntity<Dueno> update(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer id, @RequestBody @Valid DuenoDTO duenoDTO) {
        User user = findUserByJWT(authHeader);
        Dueno duenoToGet = duenoService.getDuenoById(id);
        if (duenoToGet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!(user.getAuthority().getAuthority().equals(admin) || user.getId().equals(duenoToGet.getUser().getId()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        // Check if the data is empty
        if (duenoDTO == null) {
            throw new IllegalArgumentException("Dueno no puede ser nulo");
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
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
        Dueno dueno = duenoService.getDuenoById(id);
        if (dueno == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!(user.getAuthority().getAuthority().equals(admin) || user.getId().equals(dueno.getUser().getId()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        duenoService.deleteDueno(id);
        userService.deleteUser(dueno.getUser().getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}