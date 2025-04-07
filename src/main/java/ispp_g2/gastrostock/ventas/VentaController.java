package ispp_g2.gastrostock.ventas;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final UserService userService;
    private final VentaService ventaService;
    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;
    private final NegocioService negocioService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @GetMapping
    public ResponseEntity<List<Venta>> findAll() {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(ventaService.getAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Venta> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(ventaService.getById(id),HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            Venta venta = ventaService.getById(id);
            Negocio negocio = negocioService.getById(venta.getNegocio().getId());
            if(negocio.getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(venta,HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            Venta venta = ventaService.getById(id);
            if(venta.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(venta,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<VentaDto> findByIdDto(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(ventaService.convertirVentaDto(ventaService.getById(id)),HttpStatus.OK);
        } else if(user.hasAnyAuthority("dueno").equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            Venta venta = ventaService.getById(id);
            Negocio negocio = negocioService.getById(venta.getNegocio().getId());
            if(negocio.getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(ventaService.convertirVentaDto(venta),HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority("empleado").equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            Venta venta = ventaService.getById(id);
            if(venta.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(ventaService.convertirVentaDto(venta),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Venta>> findByNegocio(@PathVariable("negocio") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(ventaService.getVentasByNegocioId(negocioId),HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(ventaService.getVentasByNegocioId(negocioId),HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                return new ResponseEntity<>(ventaService.getVentasByNegocioId(negocioId),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/negocio/{negocio}")
    public ResponseEntity<List<VentaDto>> findByNegocioDto(@PathVariable("negocio") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(ventaService.getVentasByNegocioId(negocioId)
                        .stream().map(ventaService::convertirVentaDto).toList(),HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(ventaService.getVentasByNegocioId(negocioId)
                            .stream().map(ventaService::convertirVentaDto).toList(),HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                return new ResponseEntity<>(ventaService.getVentasByNegocioId(negocioId)
                            .stream().map(ventaService::convertirVentaDto).toList(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<Venta> save(@RequestBody VentaDto ventaDto) {
        User user = userService.findCurrentUser();
        if(ventaDto == null) {
            throw new IllegalArgumentException("Venta no puede ser nulo");
        }
        if(user.hasAnyAuthority(DUENO).equals(true)) {
            Venta newVenta = ventaService.convertirVenta(ventaDto);
            Negocio negocio = negocioService.getById(ventaDto.getNegocioId());
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if(negocio.getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(ventaService.save(newVenta),HttpStatus.CREATED); 
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Venta newVenta = ventaService.convertirVenta(ventaDto);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(ventaDto.getNegocioId())) {
                return new ResponseEntity<>(ventaService.save(newVenta),HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> update(@PathVariable("id") Integer id, @RequestBody VentaDto ventaDto) {
        User user = userService.findCurrentUser();
        if(ventaDto == null) {
            throw new IllegalArgumentException("Venta no puede ser nulo");
        }
        if(user.hasAnyAuthority(DUENO).equals(true)) {
            Venta newVenta = ventaService.convertirVenta(ventaDto);
            Negocio negocio = negocioService.getById(ventaDto.getNegocioId());
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if(negocio.getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(ventaService.update(id, newVenta),HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Venta newVenta = ventaService.convertirVenta(ventaDto);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(ventaDto.getNegocioId())) {
                return new ResponseEntity<>(ventaService.update(id, newVenta),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(DUENO).equals(true)) {
            Venta venta = ventaService.getById(id);
            Negocio negocio = negocioService.getById(venta.getNegocio().getId());
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if(negocio.getDueno().getId().equals(dueno.getId())) {
                ventaService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }  
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
