package ispp_g2.gastrostock.lote;

import ispp_g2.gastrostock.categorias.CategoriaService;
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
    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";
    private final CategoriaService categoriaService;

    @Autowired
    public LoteController(LoteService loteService, UserService userService,
                          NegocioService negocioService, ReabastecimientoService reabastecimientoService,
                          DuenoService duenoService, EmpleadoService empleadoService, CategoriaService categoriaService) {
        this.loteService = loteService;
        this.userService = userService;
        this.negocioService = negocioService;
        this.reabastecimientoService = reabastecimientoService;
        this.duenoService = duenoService;
        this.empleadoService = empleadoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Lote>> findAll() {
        User user = userService.findCurrentUser();
        List<Lote> lotes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> lotes = loteService.getLotes();
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                lotes = loteService.getLotesByDuenoId(dueno.getId());
            }
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                lotes = loteService.getLotesByNegocioId(empleado.getNegocio().getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Lote lote;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> lote = loteService.getById(id);
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                lote = loteService.getLotesByDuenoId(dueno.getId()).stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                lote = loteService.getLotesByNegocioId(empleado.getNegocio().getId()).stream()
                        .filter(l -> l.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(lote, HttpStatus.OK);
    }

    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<Lote>> findByCantidad(@PathVariable("cantidad") Integer cantidad) {
        User user = userService.findCurrentUser();
        List<Lote> lotes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> lotes = loteService.getLotesByCantidad(cantidad);
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                lotes = loteService.getLotesByDuenoId(dueno.getId()).stream()
                        .filter(l -> l.getCantidad().equals(cantidad))
                        .toList();
            }
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                lotes = loteService.getLotesByNegocioId(empleado.getNegocio().getId()).stream()
                        .filter(l -> l.getCantidad().equals(cantidad))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/fechaCaducidad/{fechaCaducidad}")
    public ResponseEntity<List<Lote>> findByFechaCaducidad(@PathVariable("fechaCaducidad") LocalDate fechaCaducidad) {
        User user = userService.findCurrentUser();
        List<Lote> lotes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> lotes = loteService.getLotesByFechaCaducidad(fechaCaducidad);
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                lotes = loteService.getLotesByDuenoId(dueno.getId()).stream()
                        .filter(l -> l.getFechaCaducidad().equals(fechaCaducidad))
                        .toList();
            }
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                lotes = loteService.getLotesByNegocioId(empleado.getNegocio().getId()).stream()
                        .filter(l -> l.getFechaCaducidad().equals(fechaCaducidad))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/producto/{producto}")
    public ResponseEntity<List<Lote>> findByProductoId(@PathVariable("producto") Integer producto) {
        User user = userService.findCurrentUser();
        List<Lote> lotes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> lotes = loteService.getLotesByProductoId(producto);
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                lotes = loteService.getLotesByDuenoId(dueno.getId()).stream()
                        .filter(l -> l.getProducto().getId().equals(producto))
                        .toList();
            }
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                lotes = loteService.getLotesByNegocioId(empleado.getNegocio().getId()).stream()
                        .filter(l -> l.getProducto().getId().equals(producto))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/reabastecimiento/{reabastecimiento}")
    public ResponseEntity<List<Lote>> findByReabastecimientoId(@PathVariable("reabastecimiento") Integer reabastecimiento) {
        User user = userService.findCurrentUser();
        List<Lote> lotes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> lotes = loteService.getLotesByReabastecimientoId(reabastecimiento);
            case DUENO -> {
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                lotes = loteService.getLotesByDuenoId(dueno.getId()).stream()
                        .filter(l -> l.getReabastecimiento().getId().equals(reabastecimiento))
                        .toList();
            }
            case EMPLEADO -> {
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                lotes = loteService.getLotesByNegocioId(empleado.getNegocio().getId()).stream()
                        .filter(l -> l.getReabastecimiento().getId().equals(reabastecimiento))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
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
        User user = userService.findCurrentUser();
        Negocio negocio = categoriaService.getById(lote.getProducto().getCategoria().getId()).getNegocio();

        if(! ((user.getAuthority().getAuthority().equals(ADMIN)) ||
                (user.getAuthority().getAuthority().equals(DUENO)) && negocio.getDueno().getUser().getId().equals(user.getId()) ||
                (user.getAuthority().getAuthority().equals(EMPLEADO) && empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId())))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(loteService.save(lote), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lote> update(@PathVariable("id") Integer id, @RequestBody @Valid Lote lote) {
        User user = userService.findCurrentUser();
        Dueno currDueno = duenoService.getDuenoByUser(user.getId());
        Lote loteActual = loteService.getById(id);
        if (loteActual == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Negocio negocio = categoriaService.getById(lote.getProducto().getCategoria().getId()).getNegocio();
        Negocio negocioToUpdate = loteActual.getProducto().getCategoria().getNegocio();

        //        Si no es admin
        if(! ((user.getAuthority().getAuthority().equals(ADMIN)) ||
//                Ni dueno del negocio al que pertence la categoria del producto ni cambia a una categoria de otro negocio que no le pertenece
                (user.getAuthority().getAuthority().equals(DUENO)  &&
                        currDueno.getId().equals(negocioToUpdate.getDueno().getId()) &&
                        negocioService.getByDueno(currDueno.getId()).contains(negocio)) ||
//                Ni empleado del negocio al que pertence la categoria del producto ni cambia a una categoria de otro negocio
                (user.getAuthority().getAuthority().equals(EMPLEADO) &&
                        empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocioToUpdate.getId()) &&
                        negocio.equals(negocioToUpdate)))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        lote.setId(id);
        return new ResponseEntity<>(loteService.save(lote), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        Lote lote = loteService.getById(id);
        if (lote == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Negocio negocio = lote.getProducto().getCategoria().getNegocio();
        if(!((user.getAuthority().getAuthority().equals(ADMIN)) ||
                (user.getAuthority().getAuthority().equals(DUENO) &&
                        user.getId().equals(negocio.getDueno().getUser().getId())) ||
                (user.getAuthority().getAuthority().equals(EMPLEADO) &&
                        empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId()))))  {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        loteService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
