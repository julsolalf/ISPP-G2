package ispp_g2.gastrostock.empleado;

import java.util.List;

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

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService, UserService userService, NegocioService negocioService) {
        this.empleadoService = empleadoService;
        this.userService = userService;
        this.negocioService = negocioService;
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> findAll() {
        if (empleadoService.getAllEmpleados().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(empleadoService.getAllEmpleados(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable("id") String id) {
        Empleado empleado = empleadoService.getEmpleadoById(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> findByEmail(@PathVariable("email") String email) {
        Empleado empleado = empleadoService.getEmpleadoByEmail(email);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoById(email), HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Empleado>> findByNombre(@PathVariable("nombre") String nombre) {
        List<Empleado> empleados = empleadoService.getEmpleadoByNombre(nombre);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Empleado>> findByApellido(@PathVariable("apellido") String apellido) {
        List<Empleado> empleados = empleadoService.getEmpleadoByApellido(apellido);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Empleado> findByTelefono(@PathVariable("telefono") String telefono) {
        Empleado empleado = empleadoService.getEmpleadoByTelefono(telefono);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTelefono(telefono), HttpStatus.OK);
    }

    @GetMapping("/negocio/{id}")
    public ResponseEntity<List<Empleado>> findByNegocio(@PathVariable("id") String id) {
        List<Empleado> empleados = empleadoService.getEmpleadoByNegocio(id);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByNegocio(id), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Empleado> findByUser(@PathVariable("id") String id) {
        Empleado empleado = empleadoService.getEmpleadoByUser(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByUser(id), HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Empleado> findByTokenEmpleado(@PathVariable("token") String token) {
        Empleado empleado = empleadoService.getEmpleadoByTokenEmpleado(token);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTokenEmpleado(token), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Empleado> save(@RequestBody @Valid EmpleadoDTO empleadoDTO) {
        if(empleadoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        User usuario = userService.findUserById(empleadoDTO.getUser());
        Negocio negocio = negocioService.getById(empleadoDTO.getNegocio());
        if(usuario == null || negocio == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Empleado empleado = empleadoService.convertirDTOEmpleado(empleadoDTO, negocio, usuario);
        return new ResponseEntity<>(empleadoService.saveEmpleado(empleado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> update(@PathVariable("id") String id, @RequestBody @Valid  EmpleadoDTO empleadoDTO) {
        if(empleadoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        Empleado current_empleado= empleadoService.getEmpleadoById(id);
        Negocio negocio = negocioService.getById(empleadoDTO.getNegocio());
        if(empleadoService.getEmpleadoById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Empleado empleado = empleadoService.convertirDTOEmpleado(empleadoDTO,negocio,current_empleado.getUser());
        empleado.setId(Integer.valueOf(id));
        return new ResponseEntity<>(empleadoService.saveEmpleado(empleado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Empleado empleado = empleadoService.getEmpleadoById(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        empleadoService.deleteEmpleado(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
