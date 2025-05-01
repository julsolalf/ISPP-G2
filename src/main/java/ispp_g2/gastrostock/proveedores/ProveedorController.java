package ispp_g2.gastrostock.proveedores;

import ispp_g2.gastrostock.dueno.Dueno;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.exceptions.BadRequestException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final UserService userService;
    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;
    private final NegocioService negocioService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @Autowired
    public ProveedorController(ProveedorService proveedorService, UserService userService,
        DuenoService duenoService, EmpleadoService empleadoService, NegocioService negocioService) {
        this.proveedorService = proveedorService;
        this.userService = userService;
        this.duenoService = duenoService;
        this.empleadoService = empleadoService;
        this.negocioService = negocioService;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> findAll() {
        User user = userService.findCurrentUser();
        List<Proveedor> proveedores;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> proveedores = proveedorService.findAll();
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                proveedores = proveedorService.findProveedorByNegocioId(empleado.getNegocio().getId());
            }
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                proveedores = proveedorService.findProveedorByDuenoId(dueno.getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<ProveedorDTO>> findAllDTO() {
        User user = userService.findCurrentUser();
        List<Proveedor> proveedores;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> proveedores = proveedorService.findAll();
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                proveedores = proveedorService.findProveedorByNegocioId(empleado.getNegocio().getId());
            }
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                proveedores = proveedorService.findProveedorByDuenoId(dueno.getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(proveedores
                .stream()
                .map(proveedorService::convertirProveedorDTO)
                .toList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> findById(@PathVariable Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(proveedorService.findById(id), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Proveedor proveedor = proveedorService.findById(id);
            if(proveedor.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(proveedor, HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Proveedor proveedor = proveedorService.findById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(proveedor.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(proveedor, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<ProveedorDTO> findByIdDTO(@PathVariable Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(proveedorService.convertirProveedorDTO(proveedorService.findById(id)),
                    HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Proveedor proveedor = proveedorService.findById(id);
            if(proveedor.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(proveedorService.convertirProveedorDTO(proveedor), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Proveedor proveedor = proveedorService.findById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(proveedor.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(proveedorService.convertirProveedorDTO(proveedor), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Proveedor> findByEmail(@PathVariable("email") String email) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
           return new ResponseEntity<>(proveedorService.findByEmail(email), HttpStatus.OK); 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Proveedor> findByTelefono(@PathVariable("telefono") String telefono) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
           return new ResponseEntity<>(proveedorService.findByTelefono(telefono), HttpStatus.OK); 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/direccion/{direccion}")
    public ResponseEntity<Proveedor> findByDireccion(@PathVariable("direccion") String direccion) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
           return new ResponseEntity<>(proveedorService.findByDireccion(direccion), HttpStatus.OK); 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Proveedor> findByNombre(@PathVariable("nombre") String nombre) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
           return new ResponseEntity<>(proveedorService.findByNombre(nombre), HttpStatus.OK); 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Proveedor>> findProveedorByNegocioId(@PathVariable("negocio") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Proveedor> proveedores = proveedorService.findProveedorByNegocioId(negocioId);
            if(proveedores.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
           return new ResponseEntity<>(proveedores, HttpStatus.OK); 
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                List<Proveedor> proveedores = proveedorService.findProveedorByNegocioId(negocioId);
                if(proveedores.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            return new ResponseEntity<>(proveedores, HttpStatus.OK);           }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                List<Proveedor> proveedores = proveedorService.findProveedorByNegocioId(negocioId);
                if(proveedores.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
               return new ResponseEntity<>(proveedores, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/negocio/{negocio}")
    public ResponseEntity<List<ProveedorDTO>> findProveedorByNegocioIdDto(@PathVariable("negocio") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Proveedor> proveedores = proveedorService.findProveedorByNegocioId(negocioId);
            if(proveedores.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
           return new ResponseEntity<>(proveedores
                .stream()
                .map(proveedorService::convertirProveedorDTO)
                .toList(), HttpStatus.OK); 
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                List<Proveedor> proveedores = proveedorService.findProveedorByNegocioId(negocioId);
                if(proveedores.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            return new ResponseEntity<>(proveedores
            .stream()
            .map(proveedorService::convertirProveedorDTO)
            .toList(), HttpStatus.OK);           }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                List<Proveedor> proveedores = proveedorService.findProveedorByNegocioId(negocioId);
                if(proveedores.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
               return new ResponseEntity<>(proveedores
               .stream()
               .map(proveedorService::convertirProveedorDTO)
               .toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<Proveedor> save(@RequestBody @Valid ProveedorDTO proveedorDto) {
        if(proveedorDto == null){
            throw new IllegalArgumentException("Proveedor no puede ser nulo");
        }
        if(!validarTelefono(proveedorDto.getTelefono())) {
            throw new BadRequestException("El teléfono debe ser correcto");
        }
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(proveedorService.save(proveedorService.convertirDTOProveedor(proveedorDto)),
                    HttpStatus.CREATED);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Proveedor proveedor = proveedorService.convertirDTOProveedor(proveedorDto);
            if(proveedor.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(proveedorService.save(proveedor),
                        HttpStatus.CREATED);  
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(proveedorDto.getNegocioId())) {
            Proveedor proveedor = proveedorService.convertirDTOProveedor(proveedorDto);
                return new ResponseEntity<>(proveedorService.save(proveedor),
                        HttpStatus.CREATED); 
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> update(@PathVariable("id") Integer id, @RequestBody @Valid ProveedorDTO newProveedor) {
        User user = userService.findCurrentUser();
        if(newProveedor == null){
            throw new IllegalArgumentException("Proveedor no puede ser nulo");
        }
        if(!validarTelefono(newProveedor.getTelefono())) {
            throw new BadRequestException("El teléfono debe ser correcto");
        }
        Proveedor toUpdate = proveedorService.convertirDTOProveedor(newProveedor);
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            Proveedor proveedor = proveedorService.convertirDTOProveedor(newProveedor);
            return new ResponseEntity<>(proveedorService.update(id, proveedor), HttpStatus.OK); 
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Proveedor proveedor = proveedorService.convertirDTOProveedor(newProveedor);
            if(proveedor.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                if(proveedor.getNegocio().getId().equals(toUpdate.getNegocio().getId())) {
                    return new ResponseEntity<>(proveedorService.update(id, proveedor), HttpStatus.OK); 
                }
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Proveedor proveedor = proveedorService.convertirDTOProveedor(newProveedor);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(proveedor.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                if(proveedor.getNegocio().getId().equals(toUpdate.getNegocio().getId())) {
                    return new ResponseEntity<>(proveedorService.update(id, proveedor), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        Proveedor proveedor = proveedorService.findById(id);
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            proveedorService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(proveedor.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                proveedorService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(proveedor.getNegocio().getId())) {
                proveedorService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private boolean validarTelefono(String telefono) {
        Pattern pattern = Pattern.compile("^[6789]\\d{8}$");
        return pattern.matcher(telefono).matches();
    }
}
