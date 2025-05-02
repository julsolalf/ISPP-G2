package ispp_g2.gastrostock.carrito;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService carritoService;
    private final NegocioService negocioService;
    private final UserService userService;
    private final EmpleadoService empleadoService;
    private final ProveedorService proveedorService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @Autowired
    public CarritoController(CarritoService carritoService, NegocioService negocioService,
            UserService userService, EmpleadoService empleadoService, ProveedorService proveedorService) {
        this.carritoService = carritoService;
        this.negocioService = negocioService;
        this.userService = userService;
        this.empleadoService = empleadoService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<Carrito>> findAll() {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Carrito> carritos = carritoService.findAll();
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritos, HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            List<Carrito> carritos = carritoService.findAll()
            .stream()
            .filter(c->c.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()))
            .toList();
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritos, HttpStatus.OK); 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<Carrito> carritos = carritoService.findAll()
            .stream()
            .filter(c->c.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()))
            .toList();
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<CarritoDTO>> findAllDTO() {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Carrito> carritos = carritoService.findAll();
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritos
            .stream()
            .map(CarritoDTO::of)
            .toList(), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            List<Carrito> carritos = carritoService.findAll()
            .stream()
            .filter(c->c.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()))
            .toList();
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritos
            .stream()
            .map(CarritoDTO::of)
            .toList(), HttpStatus.OK);        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<Carrito> carritos = carritoService.findAll()
            .stream()
            .filter(c->c.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()))
            .toList();
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carritos
            .stream()
            .map(CarritoDTO::of)
            .toList(), HttpStatus.OK);        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(carritoService.findById(id), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Carrito carrito = carritoService.findById(id);
            if(carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(carrito, HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            Carrito carrito = carritoService.findById(id);
            if(carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(carrito, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<CarritoDTO> findByIdDTO(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(CarritoDTO.of(carritoService.findById(id)), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Carrito carrito = carritoService.findById(id);
            if(carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(CarritoDTO.of(carrito), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            Carrito carrito = carritoService.findById(id);
            if(carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(CarritoDTO.of(carrito), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<Carrito>> findByProveedor(@PathVariable("proveedor") Integer proveedorId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Carrito> carritos = carritoService.findByProveedorId(proveedorId);
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(carritos, HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Proveedor proveedor = proveedorService.findById(proveedorId);
            if(proveedor.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                List<Carrito> carritos = carritoService.findByProveedorId(proveedorId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(carritos, HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(proveedorService.findById(proveedorId).getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<Carrito> carritos = carritoService.findByProveedorId(proveedorId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
                }
                return new ResponseEntity<>(carritos, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/proveedor/{proveedor}")
    public ResponseEntity<List<CarritoDTO>> findByProveedorDTO(@PathVariable("proveedor") Integer proveedorId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Carrito> carritos = carritoService.findByProveedorId(proveedorId);
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(carritos
                .stream()
                .map(CarritoDTO::of)
                .toList(), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Proveedor proveedor = proveedorService.findById(proveedorId);
            if(proveedor.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                List<Carrito> carritos = carritoService.findByProveedorId(proveedorId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(carritos
                    .stream()
                    .map(CarritoDTO::of)
                    .toList(), HttpStatus.OK);
                }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(proveedorService.findById(proveedorId).getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<Carrito> carritos = carritoService.findByProveedorId(proveedorId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
                }
                return new ResponseEntity<>(carritos
                    .stream()
                    .map(CarritoDTO::of)
                    .toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Carrito>> findByNegocio(@PathVariable("negocio") Integer negocioId) {
        User user = userService.findCurrentUser();

        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Carrito> carritos = carritoService.findByNegocioId(negocioId);
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(carritos, HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(negocioService.getById(negocioId).getDueno().getUser().getId().equals(user.getId())) {
                List<Carrito> carritos = carritoService.findByNegocioId(negocioId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(carritos, HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                List<Carrito> carritos = carritoService.findByNegocioId(negocioId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(carritos, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/negocio/{negocio}")
    public ResponseEntity<List<CarritoDTO>> findByNegocioDTO(@PathVariable("negocio") Integer negocioId) {
        User user = userService.findCurrentUser();

        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Carrito> carritos = carritoService.findByNegocioId(negocioId);
            if(carritos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(carritos.stream().map(CarritoDTO::of).toList(), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(negocioService.getById(negocioId).getDueno().getUser().getId().equals(user.getId())) {
                List<Carrito> carritos = carritoService.findByNegocioId(negocioId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(carritos
                    .stream()
                    .map(CarritoDTO::of)
                    .toList(), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            // Check if the Empleado belongs to the Negocio
            if(empleado.getNegocio().getId().equals(negocioId)) {
                List<Carrito> carritos = carritoService.findByNegocioId(negocioId);
                if(carritos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(carritos
                    .stream()
                    .map(CarritoDTO::of)
                    .toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<Carrito> save(@RequestBody @Valid Carrito carrito) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.CREATED);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.CREATED);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/dto")
    public ResponseEntity<Carrito> saveDTO(@RequestBody @Valid CarritoDTO carritoDTO) {
        User user = userService.findCurrentUser();
        Carrito carrito = carritoService.convertirCarrito(carritoDTO);
        carrito.setPrecioTotal(0.);
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.CREATED);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.CREATED);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(carritoService.save(carrito), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrito> update(@PathVariable("id") Integer id, @RequestBody @Valid Carrito carrito) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
          return new ResponseEntity<>(carritoService.update(id, carrito), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
           Carrito toUpdate = carritoService.findById(id);
           if(toUpdate.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()) &&
              carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
               return new ResponseEntity<>(carritoService.update(id, carrito), HttpStatus.OK);
           } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Carrito toUpdate = carritoService.findById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(toUpdate.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()) &&
               carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
               return new ResponseEntity<>(carritoService.update(id, carrito), HttpStatus.OK);
            } 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<Carrito> updateDTO(@PathVariable("id") Integer id, @RequestBody @Valid CarritoDTO carritoDTO) {
        User user = userService.findCurrentUser();
        Carrito carrito = carritoService.convertirCarrito(carritoDTO);
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
          return new ResponseEntity<>(carritoService.update(id, carrito), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
           Carrito toUpdate = carritoService.findById(id);
           if(toUpdate.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()) &&
              carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
               return new ResponseEntity<>(carritoService.update(id, carrito), HttpStatus.OK);
           } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Carrito toUpdate = carritoService.findById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(toUpdate.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()) &&
               carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
               return new ResponseEntity<>(carritoService.update(id, carrito), HttpStatus.OK);
            } 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            carritoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Carrito toDelete = carritoService.findById(id); 
            if(toDelete.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                carritoService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Carrito toDelete = carritoService.findById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(toDelete.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                carritoService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


}
