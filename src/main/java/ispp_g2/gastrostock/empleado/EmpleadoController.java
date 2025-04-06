package ispp_g2.gastrostock.empleado;

import java.util.ArrayList;
import java.util.List;

import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final UserService userService;
    private final NegocioService negocioService;
    private final JwtService jwtService;
    private final DuenoService duenoService;

    private final String adminAuth ="admin";
    private final String empleadoAuth ="empleado";
    private final String duenoAuth = "dueno";

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService, UserService userService, NegocioService negocioService,DuenoService duenoService ,JwtService jwtService) {
        this.empleadoService = empleadoService;
        this.userService = userService;
        this.negocioService = negocioService;
        this.jwtService = jwtService;
        this.duenoService = duenoService;
    }

    private User findUserByJWT(String authToken) {
        String token =authToken.substring("Bearer ".length());
        String username = jwtService.getUserNameFromJwtToken(token);
        return userService.findUserByUsername(username);
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> findAll(@RequestHeader("Authorization") String authHeader) {
        User user = findUserByJWT(authHeader);

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
    public ResponseEntity<List<EmpleadoDTO>> findAllDTO(@RequestHeader("Authorization") String authHeader) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<Empleado> findById(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
        Empleado empleado = empleadoService.getEmpleadoById(id);

        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if( !(checkUserIsEmpleadoToGet(user, empleado) || checkEmpleadoIsFromDueno(user, empleado))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<EmpleadoDTO> findDTOById(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
        Empleado empleado = empleadoService.getEmpleadoById(id);

        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if( !(checkUserIsEmpleadoToGet(user, empleado) || checkEmpleadoIsFromDueno(user, empleado))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> findByEmail(@RequestHeader("Authorization") String authHeader,@PathVariable String email) {
        User user = findUserByJWT(authHeader);

        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Empleado empleado = empleadoService.getEmpleadoByEmail(email);
        return empleado != null ? 
            ResponseEntity.ok(empleado) : 
            ResponseEntity.notFound().build();
    }

    @GetMapping("/dto/email/{email}")
    public ResponseEntity<EmpleadoDTO> findDTOByEmail(@RequestHeader("Authorization") String authHeader,@PathVariable("email") String email) {
        User user = findUserByJWT(authHeader);

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
    public ResponseEntity<List<Empleado>> findByNombre(@RequestHeader("Authorization") String authHeader ,@PathVariable("nombre") String nombre) {
        User user = findUserByJWT(authHeader);
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Empleado> empleados = empleadoService.getEmpleadoByNombre(nombre);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @GetMapping("/dto/nombre/{nombre}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByNombre(@RequestHeader("Authorization") String authHeader,@PathVariable("nombre") String nombre) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<List<Empleado>> findByApellido(@RequestHeader("Authorization") String authHeader,@PathVariable("apellido") String apellido) {
        User user = findUserByJWT(authHeader);
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Empleado> empleados = empleadoService.getEmpleadoByApellido(apellido);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(empleadoService.getEmpleadoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/dto/apellido/{apellido}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByApellido(@RequestHeader("Authorization") String authHeader, @PathVariable("apellido") String apellido) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<Empleado> findByTelefono(@RequestHeader("Authorization") String authHeader,@PathVariable("telefono") String telefono) {
        User user = findUserByJWT(authHeader);
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Empleado empleado = empleadoService.getEmpleadoByTelefono(telefono);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTelefono(telefono), HttpStatus.OK);
    }

    @GetMapping("/dto/telefono/{telefono}")
    public ResponseEntity<EmpleadoDTO> findDTOByTelefono(@RequestHeader("Authorization") String authHeader,@PathVariable("telefono") String telefono) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<List<Empleado>> findByNegocio(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<List<EmpleadoDTO>> findDTOByNegocio(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<Empleado> findByUser(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Empleado empleado = empleadoService.getEmpleadoByUser(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByUser(id), HttpStatus.OK);
    }

    @GetMapping("/dto/user/{id}")
    public ResponseEntity<EmpleadoDTO> findDTOByUser(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<Empleado> findByTokenEmpleado(@RequestHeader("Authorization") String authHeader,@PathVariable("token") String token) {
        User user = findUserByJWT(authHeader);
        if( !(user.getAuthority().getAuthority().equals(adminAuth))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Empleado empleado = empleadoService.getEmpleadoByTokenEmpleado(token);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTokenEmpleado(token), HttpStatus.OK);
    }

    @GetMapping("/dto/token/{token}")
    public ResponseEntity<EmpleadoDTO> findDTOByTokenEmpleado(@RequestHeader("Authorization") String authHeader, @PathVariable("token") String token) {
        User user = findUserByJWT(authHeader);
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
    public ResponseEntity<Empleado> save(@RequestHeader("Authorization") String authHeader,@RequestBody @Valid EmpleadoDTO empleadoDTO) {
        User user = findUserByJWT(authHeader);
        Dueno dueno = duenoService.getDuenoByUser(user.getId());

        if(empleadoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");

        Negocio negocio = negocioService.getById(empleadoDTO.getNegocio());
        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
                (user.getAuthority().getAuthority().equals(duenoAuth) && dueno.getId().equals(negocio.getDueno().getId())))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(negocio == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Empleado empleado = empleadoService.convertirDTOEmpleado(empleadoDTO, negocio);
        if(userService.findUserByUsername(empleadoDTO.getUsername())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(empleadoService.saveEmpleado(empleado), HttpStatus.CREATED);
    }

    private boolean checkUsernameNonAvailable(String username, Integer id){
        if(userService.findUserByUsername(username) == null){
            return false;
        }
        Integer empleadoToUpdate = userService.findUserByUsername(username).getId();
        Integer currEmpleadoWithUsername = empleadoService.getEmpleadoById(id).getUser().getId();
        return !empleadoToUpdate.equals(currEmpleadoWithUsername);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> update(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id, @RequestBody @Valid  EmpleadoDTO empleadoDTO) {
        User user = findUserByJWT(authHeader);
        Dueno dueno = duenoService.getDuenoByUser(user.getId());

        // Check if the data is empty
        if(empleadoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        Negocio negocio = negocioService.getById(empleadoDTO.getNegocio());
        // Check if the employee exists
        Empleado empleadoToUpdate = empleadoService.getEmpleadoById(id);
        if(empleadoToUpdate == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if( !((user.getAuthority().getAuthority().equals(adminAuth)) ||
                // El empleado para actualizar pertenece al negocio del usuario y el negocio al que se le asigna el empleado esta dentro de los negocios del dueno
                (user.getAuthority().getAuthority().equals(duenoAuth) && dueno.getId().equals(empleadoToUpdate.getNegocio().getDueno().getId()) &&
                        negocioService.getByDueno(dueno.getId()).contains(negocio)) ||
                // El empleado para actualizar es el mismo que el usuario, y no cambia de negocio
                (user.getAuthority().getAuthority().equals(empleadoAuth) && empleadoToUpdate.getUser().getId().equals(user.getId()) &&
                        empleadoDTO.getNegocio().equals(empleadoToUpdate.getNegocio().getId())))){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Convert the DTO to an employee
        Empleado empleado = empleadoService.convertirDTOEmpleado(empleadoDTO,negocio);
        empleado.setId(id);
        // Check if the username is available
        if(checkUsernameNonAvailable(empleadoDTO.getUsername(),id)){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Save the user
        empleado.getUser().setId(empleadoService.getEmpleadoById(id).getUser().getId());
        return new ResponseEntity<>(empleadoService.saveEmpleado(empleado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authHeader,@PathVariable("id") Integer id) {
        User user = findUserByJWT(authHeader);
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
