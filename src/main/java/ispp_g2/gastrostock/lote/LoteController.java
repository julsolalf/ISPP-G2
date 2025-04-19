package ispp_g2.gastrostock.lote;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import ispp_g2.gastrostock.reabastecimiento.ReabastecimientoService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final LoteService loteService;
    private final UserService userService;
    private final NegocioService negocioService;
    private final ReabastecimientoService reabastecimientoService;
    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @Autowired
    public LoteController(LoteService loteService, UserService userService,
            NegocioService negocioService, ReabastecimientoService reabastecimientoService,
            DuenoService duenoService, EmpleadoService empleadoService) {
        this.loteService = loteService;
        this.userService = userService;
        this.negocioService = negocioService;
        this.reabastecimientoService = reabastecimientoService;
        this.duenoService = duenoService;
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<Lote>> findAll() {
        if(loteService.getLotes().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(loteService.getLotes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> findById(@PathVariable("id") Integer id) {
        Lote lote = loteService.getById(id);
        if(lote == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lote, HttpStatus.OK);
    }

    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<Lote>> findByCantidad(@PathVariable("cantidad") Integer cantidad) {
        List<Lote> lotes = loteService.getLotesByCantidad(cantidad);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/fechaCaducidad/{fechaCaducidad}")
    public ResponseEntity<List<Lote>> findByFechaCaducidad(@PathVariable("fechaCaducidad") LocalDate fechaCaducidad) {
        List<Lote> lotes = loteService.getLotesByFechaCaducidad(fechaCaducidad);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/producto/{producto}")
    public ResponseEntity<List<Lote>> findByProductoId(@PathVariable("producto") Integer producto) {
        List<Lote> lotes = loteService.getLotesByProductoId(producto);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/reabastecimiento/{reabastecimiento}")
    public ResponseEntity<List<Lote>> findByReabastecimientoId(@PathVariable("reabastecimiento") Integer reabastecimiento) {
        List<Lote> lotes = loteService.getLotesByReabastecimientoId(reabastecimiento);
        if(lotes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}/fecha/{fecha}")
    public ResponseEntity<List<Lote>> findCaducadosByNegocioId(@PathVariable("negocio") Integer negocioId, @PathVariable("fecha") LocalDate fecha) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(loteService.getLotesCaducadosByNegocioId(negocioId, fecha), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
           Negocio negocio = negocioService.getById(negocioId);
           Dueno dueno = duenoService.getDuenoByUser(user.getId());
           if(negocio.getDueno().getId().equals(dueno.getId())) {
               return new ResponseEntity<>(loteService.getLotesCaducadosByNegocioId(negocioId, fecha), HttpStatus.OK); 
           }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
           Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
           if(empleado.getNegocio().getId().equals(negocioId)) {
               return new ResponseEntity<>(loteService.getLotesCaducadosByNegocioId(negocioId, fecha), HttpStatus.OK);
           }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<Lote> save(@RequestBody @Valid Lote lote) {
        if (lote == null){
            throw new IllegalArgumentException("El lote no puede ser nulo");
        }
        return new ResponseEntity<>(loteService.save(lote), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lote> update(@PathVariable("id") Integer id, @RequestBody @Valid Lote lote) {
        if (lote == null) {
            throw new IllegalArgumentException("El lote no puede ser nulo");
        }
        if (loteService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lote.setId(id);
        return new ResponseEntity<>(loteService.save(lote), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (loteService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loteService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
