package ispp_g2.gastrostock.diaReparto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.proveedores.Proveedor;
import ispp_g2.gastrostock.proveedores.ProveedorService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/diasReparto")
public class DiaRepartoController {

    private final DiaRepartoService diaRepartoService;
    private final UserService userService;
    private final EmpleadoService empleadoService;
    private final ProveedorService proveedorService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @Autowired
    public DiaRepartoController(DiaRepartoService diaRepartoService, UserService userService,
        EmpleadoService empleadoService, ProveedorService proveedorService) {
        this.diaRepartoService = diaRepartoService;
        this.userService = userService;
        this.empleadoService = empleadoService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<DiaReparto>> findAll() {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<DiaReparto> diasReparto = diaRepartoService.getDiasReparto();
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto, HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            List<DiaReparto> diasReparto = diaRepartoService.getDiasReparto()
                    .stream()
                    .filter(d->d.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()))
                    .toList();
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto, HttpStatus.OK);
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<DiaReparto> diasReparto = diaRepartoService.getDiasReparto()
                   .stream()
                   .filter(d->d.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()))
                   .toList();
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<DiaRepartoDTO>> findAllDto() {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<DiaReparto> diasReparto = diaRepartoService.getDiasReparto();
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto.stream()
                    .map(DiaRepartoDTO::of).toList(), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            List<DiaReparto> diasReparto = diaRepartoService.getDiasReparto()
                    .stream()
                    .filter(d->d.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()))
                    .toList();
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto.stream()
                    .map(DiaRepartoDTO::of).toList(), HttpStatus.OK);
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<DiaReparto> diasReparto = diaRepartoService.getDiasReparto()
                   .stream()
                   .filter(d->d.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()))
                   .toList();
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto.stream()
                    .map(DiaRepartoDTO::of).toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaReparto> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        DiaReparto diaReparto = diaRepartoService.getById(id);
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(diaReparto, HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(diaReparto.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(diaReparto, HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(diaReparto.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(diaReparto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<DiaRepartoDTO> findByIdDto(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        DiaReparto diaReparto = diaRepartoService.getById(id);
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(DiaRepartoDTO.of(diaReparto), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(diaReparto.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(DiaRepartoDTO.of(diaReparto), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(diaReparto.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(DiaRepartoDTO.of(diaReparto), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/diaSemana/{diaSemana}")
    public ResponseEntity<List<DiaReparto>> findByDiaSemana(@PathVariable("diaSemana") String diaSemana) {
        User user = userService.findCurrentUser();
        List<DiaReparto> diasReparto = diaRepartoService.getDiaRepartoByDiaSemana(DayOfWeek.valueOf(diaSemana));
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto, HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            List<DiaReparto> filtered = diasReparto.stream()
                .filter(d->d.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId()))
                .toList();
            if(filtered.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(filtered, HttpStatus.OK);
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<DiaReparto> filtered = diasReparto.stream()
                .filter(d->d.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId()))
                .toList();
            if(filtered.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(filtered, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<DiaReparto>> findByProveedor(@PathVariable("proveedor") Integer proveedor) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            List<DiaReparto> diasReparto = diaRepartoService.getDiaRepartoByProveedorId(proveedor);
            if(diasReparto.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(diasReparto, HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Proveedor proveedorObj = proveedorService.findById(proveedor);
            if(proveedorObj.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                List<DiaReparto> diasReparto = diaRepartoService.getDiaRepartoByProveedorId(proveedor);
                if(diasReparto.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(diasReparto, HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            Proveedor proveedorObj = proveedorService.findById(proveedor);
            if(proveedorObj.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<DiaReparto> diasReparto = diaRepartoService.getDiaRepartoByProveedorId(proveedor);
                if(diasReparto.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(diasReparto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        
    }

    @PostMapping
    public ResponseEntity<DiaReparto> save(@RequestBody @Valid DiaReparto diaReparto) {
        if(diaReparto == null) {
            throw  new IllegalArgumentException("Dia de reparto no puede ser nulo");
        }
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.CREATED);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(diaReparto.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.CREATED);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(diaReparto.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/dto")
    public ResponseEntity<DiaReparto> save(@RequestBody @Valid DiaRepartoDTO diaRepartoDTO) {
        if(diaRepartoDTO == null) {
            throw  new IllegalArgumentException("Dia de reparto no puede ser nulo");
        }
        DiaReparto diaReparto = diaRepartoService.convertirDTODiaReparto(diaRepartoDTO);
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.CREATED);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(diaReparto.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.CREATED);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(diaReparto.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(diaRepartoService.save(diaReparto), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaReparto> update(@PathVariable("id") Integer id, @RequestBody @Valid DiaReparto diaReparto) {
        if(diaReparto == null) {
            throw new IllegalArgumentException("Dia de reparto no puede ser nulo");
        }
        DiaReparto existing = diaRepartoService.getById(id);
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(diaRepartoService.update(id, diaReparto), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(existing.getProveedor().getId().equals(diaReparto.getProveedor().getId()) &&
                existing.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                    return new ResponseEntity<>(diaRepartoService.update(id, diaReparto), HttpStatus.OK);
        }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(existing.getProveedor().getId().equals(diaReparto.getProveedor().getId()) &&
                existing.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                    return new ResponseEntity<>(diaRepartoService.update(id, diaReparto), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<DiaReparto> updateDto(@PathVariable("id") Integer id, @RequestBody @Valid DiaRepartoDTO diaRepartoDTO) {
        if(diaRepartoDTO == null) {
            throw new IllegalArgumentException("Dia de reparto no puede ser nulo");
        }
        DiaReparto diaReparto = diaRepartoService.convertirDTODiaReparto(diaRepartoDTO);
        DiaReparto existing = diaRepartoService.getById(id);
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(diaRepartoService.update(id, diaReparto), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(existing.getProveedor().getId().equals(diaReparto.getProveedor().getId()) &&
                existing.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                    return new ResponseEntity<>(diaRepartoService.update(id, diaReparto), HttpStatus.OK);
        }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(existing.getProveedor().getId().equals(diaReparto.getProveedor().getId()) &&
                existing.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                    return new ResponseEntity<>(diaRepartoService.update(id, diaReparto), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        DiaReparto diaReparto = diaRepartoService.getById(id);
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            diaRepartoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(diaReparto.getProveedor().getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                diaRepartoService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(diaReparto.getProveedor().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                diaRepartoService.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
