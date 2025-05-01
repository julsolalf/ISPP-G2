package ispp_g2.gastrostock.reabastecimiento;

import java.util.List;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;

@RestController
@RequestMapping("/api/reabastecimientos")
public class ReabastecimientoController {

    private final ReabastecimientoService reabastecimientoService;
    private final UserService userService;

    private final String admin = "admin";
    private final String dueno = "dueno";
    private final String empleado = "empleado";
    private final NegocioService negocioService;
    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;

    @Autowired
    public ReabastecimientoController(ReabastecimientoService reabastecimientoService, UserService userService, NegocioService negocioService, DuenoService duenoService, EmpleadoService empleadoService) {
        this.reabastecimientoService = reabastecimientoService;
        this.negocioService = negocioService;
        this.duenoService = duenoService;
        this.empleadoService = empleadoService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Reabastecimiento>> findAll() {
        User user = userService.findCurrentUser();
        List<Reabastecimiento> reabastecimientos;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimientos = reabastecimientoService.getAll();
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimientos = reabastecimientoService.getByDueno(currDueno.getId());
            }

            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocio =currEmpleado.getNegocio();
                reabastecimientos = reabastecimientoService.getByNegocio(negocio.getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reabastecimiento> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Reabastecimiento reabastecimiento;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimiento = reabastecimientoService.getById(id);
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimiento = reabastecimientoService.getByDueno(currDueno.getId()).stream()
                        .filter(r -> r.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocio =currEmpleado.getNegocio();
                reabastecimiento = reabastecimientoService.getByNegocio(negocio.getId()).stream()
                        .filter(r -> r.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimiento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimiento, HttpStatus.OK);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Reabastecimiento>> findByFecha(@PathVariable("fecha") LocalDate fecha) {
        User user = userService.findCurrentUser();
        List<Reabastecimiento> reabastecimientos;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimientos = reabastecimientoService.getByFecha(fecha);
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimientos = reabastecimientoService.getByDueno(currDueno.getId()).stream()
                        .filter(r -> r.getFecha().equals(fecha))
                        .toList();
            }
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocio =currEmpleado.getNegocio();
                reabastecimientos = reabastecimientoService.getByNegocio(negocio.getId()).stream()
                        .filter(r -> r.getFecha().equals(fecha))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/precioTotal/{precioTotal}")
    public ResponseEntity<List<Reabastecimiento>> findByPrecioTotal(@PathVariable("precioTotal") Double precioTotal) {
        User user = userService.findCurrentUser();
        List<Reabastecimiento> reabastecimientos;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimientos = reabastecimientoService.getByPrecioTotal(precioTotal);
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimientos = reabastecimientoService.getByDueno(currDueno.getId()).stream()
                        .filter(r -> r.getPrecioTotal().equals(precioTotal))
                        .toList();
            }
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocio =currEmpleado.getNegocio();
                reabastecimientos = reabastecimientoService.getByNegocio(negocio.getId()).stream()
                        .filter(r -> r.getPrecioTotal().equals(precioTotal))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/referencia/{referencia}")
    public ResponseEntity<List<Reabastecimiento>> findByReferencia(@PathVariable("referencia") String referencia) {
        User user = userService.findCurrentUser();
        List<Reabastecimiento> reabastecimientos;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimientos = reabastecimientoService.getByReferencia(referencia);
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimientos = reabastecimientoService.getByDueno(currDueno.getId()).stream()
                        .filter(r -> r.getReferencia().equals(referencia))
                        .toList();
            }
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocio =currEmpleado.getNegocio();
                reabastecimientos = reabastecimientoService.getByNegocio(negocio.getId()).stream()
                        .filter(r -> r.getReferencia().equals(referencia))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Reabastecimiento>> findByNegocio(@PathVariable("negocio") Integer negocio) {
        User user = userService.findCurrentUser();
        List<Reabastecimiento> reabastecimientos;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimientos = reabastecimientoService.getByNegocio(negocio);
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimientos = reabastecimientoService.getByDueno(currDueno.getId()).stream()
                        .filter(r -> r.getNegocio().getId().equals(negocio))
                        .toList();
            }
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocioActual =currEmpleado.getNegocio();
                if(!negocioActual.getId().equals(negocio)){
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                reabastecimientos = reabastecimientoService.getByNegocio(negocio).stream()
                        .filter(r -> r.getNegocio().getId().equals(negocio))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<Reabastecimiento>> findByProveedor(@PathVariable("proveedor") Integer proveedor) {
        User user = userService.findCurrentUser();
        List<Reabastecimiento> reabastecimientos;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimientos = reabastecimientoService.getByProveedor(proveedor);
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimientos = reabastecimientoService.getByDueno(currDueno.getId()).stream()
                        .filter(r -> r.getProveedor().getId().equals(proveedor))
                        .toList();
            }
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocio =currEmpleado.getNegocio();
                reabastecimientos = reabastecimientoService.getByNegocio(negocio.getId()).stream()
                        .filter(r -> r.getProveedor().getId().equals(proveedor))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @GetMapping("/fecha/{fechaInicio}/{fechaFin}")
    public ResponseEntity<List<Reabastecimiento>> findByFechaBetween(@PathVariable("fechaInicio") LocalDate fechaInicio, @PathVariable("fechaFin") LocalDate fechaFin) {
        User user = userService.findCurrentUser();
        List<Reabastecimiento> reabastecimientos;
        switch (user.getAuthority().getAuthority()){
            case admin -> reabastecimientos = reabastecimientoService.getByFechaBetween(fechaInicio, fechaFin);
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                reabastecimientos = reabastecimientoService.getByDueno(currDueno.getId()).stream()
                        .filter(r -> r.getFecha().isAfter(fechaInicio) && r.getFecha().isBefore(fechaFin))
                        .toList();
            }
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                Negocio negocio =currEmpleado.getNegocio();
                reabastecimientos = reabastecimientoService.getByNegocio(negocio.getId()).stream()
                        .filter(r -> r.getFecha().isAfter(fechaInicio) && r.getFecha().isBefore(fechaFin))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if(reabastecimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reabastecimientos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reabastecimiento> save(@RequestBody @Valid Reabastecimiento reabastecimiento) {
        User user = userService.findCurrentUser();
        if (reabastecimiento == null || reabastecimiento.getNegocio() == null || reabastecimiento.getNegocio().getId() == null) {
            throw new IllegalArgumentException("Datos de reabastecimiento inválidos");
        }

        Negocio negocioCompleto = negocioService.getById(reabastecimiento.getNegocio().getId());
        reabastecimiento.setNegocio(negocioCompleto);

        if(! ((user.getAuthority().getAuthority().equals(admin)) ||
                (user.getAuthority().getAuthority().equals(dueno)) && negocioCompleto.getDueno().getUser().getId().equals(user.getId()) ||
                (user.getAuthority().getAuthority().equals(empleado) && empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocioCompleto.getId())))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(reabastecimientoService.save(reabastecimiento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reabastecimiento> update(@PathVariable("id") Integer id, @RequestBody @Valid Reabastecimiento reabastecimiento) {
        User user = userService.findCurrentUser();
        Dueno currDueno = duenoService.getDuenoByUser(user.getId());
        Reabastecimiento existingReabastecimiento = reabastecimientoService.getById(id);
        if(reabastecimiento == null) {
            throw new IllegalArgumentException("Reabastecimiento no puede ser nulo");
        }
        if(existingReabastecimiento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Negocio negocio = reabastecimientoService.getById(reabastecimiento.getId()).getNegocio();
        Negocio negocioToUpdate = reabastecimientoService.getById(id).getNegocio();

        if(! ((user.getAuthority().getAuthority().equals(admin)) ||
                (user.getAuthority().getAuthority().equals(dueno)  &&
                        currDueno.getId().equals(negocioToUpdate.getDueno().getId()) &&
                        negocioService.getByDueno(currDueno.getId()).contains(negocio)) ||
                (user.getAuthority().getAuthority().equals(empleado) &&
                        empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocioToUpdate.getId()) &&
                        negocio.equals(negocioToUpdate)))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        reabastecimiento.setId(id);
        return new ResponseEntity<>(reabastecimientoService.save(reabastecimiento), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Reabastecimiento reabastecimiento = reabastecimientoService.getById(id);
        if(reabastecimiento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Negocio negocio = reabastecimiento.getNegocio();
        if(!((user.getAuthority().getAuthority().equals(admin)) ||
                (user.getAuthority().getAuthority().equals(dueno) &&
                        user.getId().equals(negocio.getDueno().getUser().getId())) ||
                (user.getAuthority().getAuthority().equals(empleado) &&
                        empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId()))))  {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        reabastecimientoService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
