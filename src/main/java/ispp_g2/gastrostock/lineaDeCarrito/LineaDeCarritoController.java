package ispp_g2.gastrostock.lineaDeCarrito;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.carrito.CarritoService;
import ispp_g2.gastrostock.carrito.Carrito;

import java.util.List;

@RestController
@RequestMapping("api/lineasDeCarrito")
public class LineaDeCarritoController {

    private final LineaDeCarritoService lineaDeCarritoService;
    private final CarritoService carritoService;
    private final UserService userService;
    private final EmpleadoService empleadoService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @Autowired
    public LineaDeCarritoController(
            LineaDeCarritoService lineaDeCarritoService,
            CarritoService carritoService,
            UserService userService,
            EmpleadoService empleadoService) {
        this.lineaDeCarritoService = lineaDeCarritoService;
        this.carritoService = carritoService;
        this.userService = userService;
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<LineaDeCarrito>> findAll() {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findAll();
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineas, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findAll()
                .stream()
                .filter(l -> l.getCarrito() != null &&
                        l.getCarrito().getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()))
                .toList();
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineas, HttpStatus.OK);
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findAll()
                .stream()
                .filter(l -> l.getCarrito() != null &&
                        l.getCarrito().getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()))
                .toList();
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineas, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<LineaDeCarritoDTO>> findAllDTO() {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findAll();
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineas
                .stream()
                .map(LineaDeCarritoDTO::of)
                .toList(), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findAll()
                .stream()
                .filter(l -> l.getCarrito() != null &&
                        l.getCarrito().getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()))
                .toList();
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineas
                .stream()
                .map(LineaDeCarritoDTO::of)
                .toList(), HttpStatus.OK);
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findAll()
                .stream()
                .filter(l -> l.getCarrito() != null &&
                        l.getCarrito().getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()))
                .toList();
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineas
                .stream()
                .map(LineaDeCarritoDTO::of)
                .toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaDeCarrito> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        LineaDeCarrito linea = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (linea == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Carrito carrito = linea.getCarrito();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(linea, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito != null && carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(linea, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito != null && carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(linea, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<LineaDeCarritoDTO> findByIdDTO(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        LineaDeCarrito linea = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (linea == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Carrito carrito = linea.getCarrito();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(LineaDeCarritoDTO.of(linea), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito != null && carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(LineaDeCarritoDTO.of(linea), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito != null && carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(LineaDeCarritoDTO.of(linea), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/carrito/{carritoId}")
    public ResponseEntity<List<LineaDeCarrito>> findByCarritoId(@PathVariable("carritoId") Integer carritoId) {
        User user = userService.findCurrentUser();
        Carrito carrito = carritoService.findById(carritoId);
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoId(carritoId);
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineas, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoId(carritoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoId(carritoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/carrito/{carritoId}")
    public ResponseEntity<List<LineaDeCarritoDTO>> findByCarritoIdDTO(@PathVariable("carritoId") Integer carritoId) {
        User user = userService.findCurrentUser();
        Carrito carrito = carritoService.findById(carritoId);
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoId(carritoId);
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineas
                        .stream()
                        .map(LineaDeCarritoDTO::of)
                        .toList(), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoId(carritoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas
                            .stream()
                            .map(LineaDeCarritoDTO::of)
                            .toList(), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoId(carritoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas
                            .stream()
                            .map(LineaDeCarritoDTO::of)
                            .toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/carrito/{carritoId}/producto/{productoId}")
    public ResponseEntity<List<LineaDeCarrito>> findByCarritoIdAndProductoId(
            @PathVariable("carritoId") Integer carritoId,
            @PathVariable("productoId") Integer productoId) {
        User user = userService.findCurrentUser();
        Carrito carrito = carritoService.findById(carritoId);
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(carritoId, productoId);
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineas, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(carritoId, productoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(carritoId, productoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/carrito/{carritoId}/producto/{productoId}")
    public ResponseEntity<List<LineaDeCarritoDTO>> findByCarritoIdAndProductoIdDTO(
            @PathVariable("carritoId") Integer carritoId,
            @PathVariable("productoId") Integer productoId) {
        User user = userService.findCurrentUser();
        Carrito carrito = carritoService.findById(carritoId);
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(carritoId, productoId);
            if (lineas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineas
                        .stream()
                        .map(LineaDeCarritoDTO::of)
                        .toList(), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(carritoId, productoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas
                            .stream()
                            .map(LineaDeCarritoDTO::of)
                            .toList(), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<LineaDeCarrito> lineas = lineaDeCarritoService.findLineaDeCarritoByCarritoIdAndProductoId(carritoId, productoId);
                if (lineas.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineas
                            .stream()
                            .map(LineaDeCarritoDTO::of)
                            .toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<LineaDeCarrito> save(@RequestBody @Valid LineaDeCarrito lineaDeCarrito) {
        User user = userService.findCurrentUser();
        if (lineaDeCarrito == null) {
            throw new IllegalArgumentException("La línea de carrito no puede ser nula");
        }
        Carrito carrito = lineaDeCarrito.getCarrito();
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        carrito = carritoService.findById(carrito.getId());
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.CREATED);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.CREATED);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/dto")
    public ResponseEntity<LineaDeCarrito> save(@RequestBody @Valid LineaDeCarritoDTO lineaDeCarritoDTO) {
        User user = userService.findCurrentUser();
        LineaDeCarrito lineaDeCarrito = lineaDeCarritoService.convertirLineaDeCarrito(lineaDeCarritoDTO);
        if (lineaDeCarrito == null) {
            throw new IllegalArgumentException("La línea de carrito no puede ser nula");
        }
        Carrito carrito = lineaDeCarrito.getCarrito();
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        carrito = carritoService.findById(carrito.getId());
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.CREATED);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.CREATED);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaDeCarrito> update(@PathVariable("id") Integer id, @RequestBody @Valid LineaDeCarrito lineaDeCarrito) {
        User user = userService.findCurrentUser();
        if (lineaDeCarrito == null) {
            throw new IllegalArgumentException("La línea de carrito no puede ser nula");
        }
        LineaDeCarrito lineaToUpdate = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (lineaToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Carrito carrito = lineaToUpdate.getCarrito();
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            lineaDeCarrito.setId(id);
            return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                lineaDeCarrito.setId(id);
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                lineaDeCarrito.setId(id);
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<LineaDeCarrito> updateDTO(@PathVariable("id") Integer id, @RequestBody @Valid LineaDeCarritoDTO lineaDeCarritoDTO) {
        User user = userService.findCurrentUser();
        LineaDeCarrito lineaDeCarrito = lineaDeCarritoService.convertirLineaDeCarrito(lineaDeCarritoDTO);
        if (lineaDeCarrito == null) {
            throw new IllegalArgumentException("La línea de carrito no puede ser nula");
        }
        LineaDeCarrito lineaToUpdate = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (lineaToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Carrito carrito = lineaToUpdate.getCarrito();
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            lineaDeCarrito.setId(id);
            return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                lineaDeCarrito.setId(id);
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                lineaDeCarrito.setId(id);
                return new ResponseEntity<>(lineaDeCarritoService.save(lineaDeCarrito), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        LineaDeCarrito linea = lineaDeCarritoService.findLineaDeCarritoById(id);
        if (linea == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Carrito carrito = linea.getCarrito();
        if (carrito == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            lineaDeCarritoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (carrito.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                lineaDeCarritoService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (carrito.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                lineaDeCarritoService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
