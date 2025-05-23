package ispp_g2.gastrostock.empleado;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.exceptions.BadRequestException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.AuthoritiesService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final UserService userService;
    private final NegocioService negocioService;
    private final DuenoService duenoService;
    private final PasswordEncoder encoder;
    private final AuthoritiesService authoritiesService;

    private static final String adminAuth ="admin";
    private static final String empleadoAuth ="empleado";
    private static final String duenoAuth = "dueno";

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService, UserService userService, NegocioService negocioService,
    DuenoService duenoService, PasswordEncoder encoder, AuthoritiesService authoritiesService) {
        this.empleadoService = empleadoService;
        this.userService = userService;
        this.negocioService = negocioService;
        this.duenoService = duenoService;
        this.encoder = encoder;
        this.authoritiesService = authoritiesService;
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> findAll() {
        User user = userService.findCurrentUser();

        if(user.getAuthority().getAuthority().equals(adminAuth)){
            if (empleadoService.getAllEmpleados().isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(empleadoService.getAllEmpleados(), HttpStatus.OK);
        }

        if(user.getAuthority().getAuthority().equals(empleadoAuth)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Integer duenoId=duenoService.getDuenoByUser(user.getId()).getId();
        return new ResponseEntity<>(empleadoService.getEmpleadoByDueno(duenoId), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<EmpleadoDTO>> findAllDTO() {
        User user = userService.findCurrentUser();
        if(user.getAuthority().getAuthority().equals(adminAuth)){

            if(empleadoService.getAllEmpleados().isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();

            for (Empleado empleado : empleadoService.getAllEmpleados()) {
                empleadoDTOS.add(empleadoService.convertirEmpleadoDTO(empleado));
            }
            return new ResponseEntity<>(empleadoDTOS, HttpStatus.OK);
        }

        if(user.getAuthority().getAuthority().equals(empleadoAuth)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Integer duenoId= duenoService.getDuenoByUser(user.getId()).getId();
        List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();

        for (Empleado empleado : empleadoService.getEmpleadoByDueno(duenoId)) {
            empleadoDTOS.add(empleadoService.convertirEmpleadoDTO(empleado));
        }

        return new ResponseEntity<>(empleadoDTOS, HttpStatus.OK);
    }

//    Comprobar si el usuario es empleado y si el ID del empleado a obtener es igual al ID del usuario
    private boolean checkUserIsEmpleadoToGet(User user, Empleado empleado){
        String currUserAuthority = user.getAuthority().getAuthority();
        Integer empleadoToGetUserId = empleado.getUser().getId();
        return currUserAuthority.equals(empleadoAuth) && empleadoToGetUserId.equals(user.getId());
    }

//    Comprobar si el usuario es dueno y si el ID del dueno del negocio al que pertenece el empleado es igual al ID del usuario
    private boolean checkEmpleadoIsFromDueno(User user, Empleado empleado){
        String currUserAuthority = user.getAuthority().getAuthority();
        Integer empleadoToGetDuenoId = empleado.getNegocio().getDueno().getUser().getId();
        return currUserAuthority.equals(duenoAuth) && empleadoToGetDuenoId.equals(user.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Empleado empleado = empleadoService.getEmpleadoById(id);

        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if( !(checkUserIsEmpleadoToGet(user, empleado) || checkEmpleadoIsFromDueno(user, empleado) || user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<EmpleadoDTO> findDTOById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Empleado empleado = empleadoService.getEmpleadoById(id);

        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if( !(checkUserIsEmpleadoToGet(user, empleado) || checkEmpleadoIsFromDueno(user, empleado) || user.getAuthority().getAuthority().equals(adminAuth))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> findByEmail(@PathVariable String email) {
        User user = userService.findCurrentUser();

        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Empleado empleado = empleadoService.getEmpleadoByEmail(email);
        return empleado != null ? 
            ResponseEntity.ok(empleado) : 
            ResponseEntity.notFound().build();
    }

    @GetMapping("/dto/email/{email}")
    public ResponseEntity<EmpleadoDTO> findDTOByEmail(@PathVariable("email") String email) {
        User user = userService.findCurrentUser();

        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Empleado empleado = empleadoService.getEmpleadoByEmail(email);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Empleado>> findByNombre(@PathVariable("nombre") String nombre) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Empleado> empleados = empleadoService.getEmpleadoByNombre(nombre);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @GetMapping("/dto/nombre/{nombre}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByNombre(@PathVariable("nombre") String nombre) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Empleado> empleados = empleadoService.getEmpleadoByNombre(nombre);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();
        for (Empleado empleado : empleados) {
            empleadoDTOS.add(empleadoService.convertirEmpleadoDTO(empleado));
        }

        return new ResponseEntity<>(empleadoDTOS, HttpStatus.OK);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Empleado>> findByApellido(@PathVariable("apellido") String apellido) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Empleado> empleados = empleadoService.getEmpleadoByApellido(apellido);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(empleadoService.getEmpleadoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/dto/apellido/{apellido}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByApellido(@PathVariable("apellido") String apellido) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Empleado> empleados = empleadoService.getEmpleadoByApellido(apellido);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();
        for (Empleado empleado : empleados) {
            empleadoDTOS.add(empleadoService.convertirEmpleadoDTO(empleado));
        }

        return new ResponseEntity<>(empleadoDTOS, HttpStatus.OK);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Empleado> findByTelefono(@PathVariable("telefono") String telefono) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Empleado empleado = empleadoService.getEmpleadoByTelefono(telefono);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTelefono(telefono), HttpStatus.OK);
    }

    @GetMapping("/dto/telefono/{telefono}")
    public ResponseEntity<EmpleadoDTO> findDTOByTelefono(@PathVariable("telefono") String telefono) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Empleado empleado = empleadoService.getEmpleadoByTelefono(telefono);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/negocio/{id}")
    public ResponseEntity<List<Empleado>> findByNegocio(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Dueno dueno = duenoService.getDuenoByUser(user.getId());
        Negocio negocio = negocioService.getById(id);
        List<Empleado> empleados = empleadoService.getEmpleadoByNegocio(id);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
//                Validacion que comprueba que si es dueno, el ID del dueno del negocio al que pertenece el empleado es igual al ID del dueno
                ((user.getAuthority().getAuthority().equals(duenoAuth)) && (dueno.getId().equals(negocio.getDueno().getId()))))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(empleadoService.getEmpleadoByNegocio(id), HttpStatus.OK);
    }

    @GetMapping("/dto/negocio/{id}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByNegocio(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Dueno dueno = duenoService.getDuenoByUser(user.getId());
        Negocio negocio = negocioService.getById(id);

        List<Empleado> empleados = empleadoService.getEmpleadoByNegocio(id);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();
        for (Empleado empleado : empleados) {
            empleadoDTOS.add(empleadoService.convertirEmpleadoDTO(empleado));
        }
        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
//                Validacion que comprueba que si es dueno, el ID del dueno del negocio al que pertenece el empleado es igual al ID del dueno
                ((user.getAuthority().getAuthority().equals(duenoAuth)) && (dueno.getId().equals(negocio.getDueno().getId()))))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(empleadoDTOS, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Empleado> findByUser(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth)|| user.getId().equals(id))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Empleado empleado = empleadoService.getEmpleadoByUser(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByUser(id), HttpStatus.OK);
    }

    @GetMapping("/dto/user/{id}")
    public ResponseEntity<EmpleadoDTO> findDTOByUser(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Empleado empleado = empleadoService.getEmpleadoByUser(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Empleado> findByTokenEmpleado(@PathVariable("token") String token) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Empleado empleado = empleadoService.getEmpleadoByTokenEmpleado(token);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTokenEmpleado(token), HttpStatus.OK);
    }

    @GetMapping("/dto/token/{token}")
    public ResponseEntity<EmpleadoDTO> findDTOByTokenEmpleado(@PathVariable("token") String token) {
        User user = userService.findCurrentUser();
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Empleado empleado = empleadoService.getEmpleadoByTokenEmpleado(token);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Empleado> save(@RequestBody @Valid EmpleadoDTO empleadoDTO) {
        User user = userService.findCurrentUser();
        Dueno dueno = duenoService.getDuenoByUser(user.getId());

        if(empleadoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");

        if(!validarPassword(empleadoDTO.getPassword())) {
            throw new BadRequestException("La contraseña debe tener entre 8 y 32 caracteres, 1 mayúscula, " +
            "1 minúscula, un número y un caracter especial");
        }
        if(!validarTelefono(empleadoDTO.getNumTelefono())) {
            throw new BadRequestException("El teléfono debe ser correcto");
        }

        Negocio negocio = negocioService.getById(empleadoDTO.getNegocio());
        if(negocio == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
                (user.getAuthority().getAuthority().equals(duenoAuth) && dueno.getId().equals(negocio.getDueno().getId())))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        User newUser = new User();
        newUser.setUsername(empleadoDTO.getUsername());
        newUser.setPassword(encoder.encode(empleadoDTO.getPassword()));
        newUser.setAuthority(authoritiesService.findByAuthority(empleadoAuth));
        userService.saveUser(newUser);
        
        Empleado empleado = new Empleado();
        empleado.setFirstName(empleadoDTO.getFirstName());
        empleado.setLastName(empleadoDTO.getLastName());
        empleado.setEmail(empleadoDTO.getEmail());
        empleado.setNegocio(negocio);
        empleado.setUser(newUser);
        empleado.setNumTelefono(empleadoDTO.getNumTelefono());
        empleado.setTokenEmpleado(generarToken()+newUser.getId());
        empleado.setDescripcion(empleadoDTO.getDescripcion());
        

        return new ResponseEntity<>(empleadoService.saveEmpleado(empleado), HttpStatus.CREATED);
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


    private String generarToken() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Integer l = 30;
        Random r = new Random();

        StringBuilder sb = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            int index = r.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return "gst-" + sb.toString();
    }

    private boolean checkUsernameNonAvailable(String username, Integer id){
        if(userService.findUserByUsernameNull(username) == null){
            return false;
        }
        Integer empleadoToUpdate = userService.findUserByUsername(username).getId();
        Integer currEmpleadoWithUsername = empleadoService.getEmpleadoById(id).getUser().getId();
        return !empleadoToUpdate.equals(currEmpleadoWithUsername);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> update(@PathVariable("id") Integer id, @RequestBody @Valid  EmpleadoDTO empleadoDTO) {
        User user = userService.findCurrentUser();
        if(empleadoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");

        if(!validarPassword(empleadoDTO.getPassword())) {
            throw new BadRequestException("La contraseña debe tener entre 8 y 32 caracteres, 1 mayúscula, " +
            "1 minúscula, un número y un caracter especial");
        }
        if(!validarTelefono(empleadoDTO.getNumTelefono())) {
            throw new BadRequestException("El teléfono debe ser correcto");
        }
        if(checkUsernameNonAvailable(empleadoDTO.getUsername(), id)){
            throw new BadRequestException("El nombre de usuario ya está en uso"); 
        }

        Empleado toUpdate = empleadoService.getEmpleadoById(id);
        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
                (user.getAuthority().getAuthority().equals(duenoAuth) && user.getId().equals(toUpdate.getNegocio().getDueno().getUser().getId()))||
                (user.getAuthority().getAuthority().equals(empleadoAuth) && empleadoService.getEmpleadoByUser(user.getId()).getId().equals(id)))){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User newUser = new User();
        newUser.setUsername(empleadoDTO.getUsername());
        newUser.setPassword(encoder.encode(empleadoDTO.getPassword()));
        userService.updateUser(toUpdate.getUser().getId(), newUser);
        Empleado newEmpleado = new Empleado();
        newEmpleado.setFirstName(empleadoDTO.getFirstName());
        newEmpleado.setLastName(empleadoDTO.getLastName());
        newEmpleado.setEmail(empleadoDTO.getEmail());
        newEmpleado.setNumTelefono(empleadoDTO.getNumTelefono());
        newEmpleado.setDescripcion(empleadoDTO.getDescripcion());
        empleadoService.update(id, newEmpleado);

        return new ResponseEntity<>(empleadoService.getEmpleadoById(id), HttpStatus.OK);

        
       
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Dueno dueno = duenoService.getDuenoByUser(user.getId());

        Empleado empleado = empleadoService.getEmpleadoById(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
                (user.getAuthority().getAuthority().equals(duenoAuth)) && empleado.getNegocio().getDueno().getId().equals(dueno.getId())) ){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        empleadoService.deleteEmpleado(id);
        userService.deleteUser(empleado.getUser().getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
