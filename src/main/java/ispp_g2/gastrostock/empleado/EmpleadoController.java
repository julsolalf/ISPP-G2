package ispp_g2.gastrostock.empleado;

import java.util.ArrayList;
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

    @GetMapping("/dto")
    public ResponseEntity<List<EmpleadoDTO>> findAllDTO() {
        if (empleadoService.getAllEmpleados().isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();
        for (Empleado empleado : empleadoService.getAllEmpleados()) {
            empleadoDTOS.add(empleadoService.convertirEmpleadoDTO(empleado));
        }
        return new ResponseEntity<>(empleadoDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable("id") Integer id) {
        Empleado empleado = empleadoService.getEmpleadoById(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<EmpleadoDTO> findDTOById(@PathVariable("id") Integer id) {
        Empleado empleado = empleadoService.getEmpleadoById(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> findByEmail(@PathVariable("email") String email) {
        Empleado empleado = empleadoService.getEmpleadoByEmail(email);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/dto/email/{email}")
    public ResponseEntity<EmpleadoDTO> findDTOByEmail(@PathVariable("email") String email) {
        Empleado empleado = empleadoService.getEmpleadoByEmail(email);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Empleado>> findByNombre(@PathVariable("nombre") String nombre) {
        List<Empleado> empleados = empleadoService.getEmpleadoByNombre(nombre);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @GetMapping("/dto/nombre/{nombre}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByNombre(@PathVariable("nombre") String nombre) {
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
        List<Empleado> empleados = empleadoService.getEmpleadoByApellido(apellido);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByApellido(apellido), HttpStatus.OK);
    }

    @GetMapping("/dto/apellido/{apellido}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByApellido(@PathVariable("apellido") String apellido) {
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
        Empleado empleado = empleadoService.getEmpleadoByTelefono(telefono);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTelefono(telefono), HttpStatus.OK);
    }

    @GetMapping("/dto/telefono/{telefono}")
    public ResponseEntity<EmpleadoDTO> findDTOByTelefono(@PathVariable("telefono") String telefono) {
        Empleado empleado = empleadoService.getEmpleadoByTelefono(telefono);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/negocio/{id}")
    public ResponseEntity<List<Empleado>> findByNegocio(@PathVariable("id") Integer id) {
        List<Empleado> empleados = empleadoService.getEmpleadoByNegocio(id);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByNegocio(id), HttpStatus.OK);
    }

    @GetMapping("/dto/negocio/{id}")
    public ResponseEntity<List<EmpleadoDTO>> findDTOByNegocio(@PathVariable("id") Integer id) {
        List<Empleado> empleados = empleadoService.getEmpleadoByNegocio(id);
        if(empleados.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<EmpleadoDTO> empleadoDTOS = new ArrayList<>();
        for (Empleado empleado : empleados) {
            empleadoDTOS.add(empleadoService.convertirEmpleadoDTO(empleado));
        }
        return new ResponseEntity<>(empleadoDTOS, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Empleado> findByUser(@PathVariable("id") Integer id) {
        Empleado empleado = empleadoService.getEmpleadoByUser(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByUser(id), HttpStatus.OK);
    }

    @GetMapping("/dto/user/{id}")
    public ResponseEntity<EmpleadoDTO> findDTOByUser(@PathVariable("id") Integer id) {
        Empleado empleado = empleadoService.getEmpleadoByUser(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Empleado> findByTokenEmpleado(@PathVariable("token") String token) {
        Empleado empleado = empleadoService.getEmpleadoByTokenEmpleado(token);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(empleadoService.getEmpleadoByTokenEmpleado(token), HttpStatus.OK);
    }

    @GetMapping("/dto/token/{token}")
    public ResponseEntity<EmpleadoDTO> findDTOByTokenEmpleado(@PathVariable("token") String token) {
        Empleado empleado = empleadoService.getEmpleadoByTokenEmpleado(token);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EmpleadoDTO empleadoDTO = empleadoService.convertirEmpleadoDTO(empleado);
        return new ResponseEntity<>(empleadoDTO, HttpStatus.OK);
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
    public ResponseEntity<Empleado> update(@PathVariable("id") Integer id, @RequestBody @Valid  EmpleadoDTO empleadoDTO) {
        if(empleadoDTO==null)
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        Empleado current_empleado= empleadoService.getEmpleadoById(id);
        Negocio negocio = negocioService.getById(empleadoDTO.getNegocio());
        if(empleadoService.getEmpleadoById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Empleado empleado = empleadoService.convertirDTOEmpleado(empleadoDTO,negocio,current_empleado.getUser());
        empleado.setId(id);
        return new ResponseEntity<>(empleadoService.saveEmpleado(empleado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        Empleado empleado = empleadoService.getEmpleadoById(id);
        if(empleado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        empleadoService.deleteEmpleado(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
