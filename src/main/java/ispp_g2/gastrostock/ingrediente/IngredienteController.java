package ispp_g2.gastrostock.ingrediente;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";
    private final UserService userService;
    private final EmpleadoService empleadoService;
    private final DuenoService duenoService;
    private final NegocioService negocioService;
    private final ProductoInventarioService productoInventarioService;

    @Autowired
    public IngredienteController(IngredienteService ingredienteService, UserService userService, EmpleadoService empleadoService, DuenoService duenoService, NegocioService negocioService, ProductoInventarioService productoInventarioService) {
        this.ingredienteService = ingredienteService;
        this.userService = userService;
        this.empleadoService = empleadoService;
        this.duenoService = duenoService;
        this.negocioService = negocioService;
        this.productoInventarioService = productoInventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Ingrediente>> findAll() {
        User user = userService.findCurrentUser();
        List<Ingrediente> ingredientes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> ingredientes = ingredienteService.getIngredientes();
            case EMPLEADO -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId());
            }
            case DUENO -> {
                Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByDuenoId(currDueno.getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<IngredienteDTO>> findAllDTO() {
        User user = userService.findCurrentUser();
        List<Ingrediente> ingredientes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> ingredientes = ingredienteService.getIngredientes();
            case EMPLEADO -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId());
            }
            case DUENO -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByDuenoId(currDueno.getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        List<IngredienteDTO> ingredienteDTO = ingredientes.stream()
                .map(ingredienteService::convertirIngredienteToDTO)
                .toList();
        return new ResponseEntity<>(ingredienteDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> findById(@PathVariable("id") Integer id) {
        try {
            User user = userService.findCurrentUser();
            Ingrediente ingrediente;
            switch (user.getAuthority().getAuthority()){
                case ADMIN -> ingrediente = ingredienteService.getById(id);
                case EMPLEADO -> {
                    Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                    ingrediente = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId()).stream()
                            .filter(ing -> ing.getId().equals(id))
                            .findFirst()
                            .orElse(null);
                }
                case DUENO -> {
                    Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
                    ingrediente = ingredienteService.getIngredientesByDuenoId(currDueno.getId()).stream()
                            .filter(ing -> ing.getId().equals(id))
                            .findFirst()
                            .orElse(null);
                }
                default -> {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            return new ResponseEntity<>(ingrediente, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<IngredienteDTO> findDTOById(@PathVariable("id") Integer id) {
        try {
            User user = userService.findCurrentUser();
            Ingrediente ingrediente;
            switch (user.getAuthority().getAuthority()){
                case ADMIN -> ingrediente = ingredienteService.getById(id);
                case EMPLEADO -> {
                    Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                    ingrediente = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId()).stream()
                            .filter(ing -> ing.getId().equals(id))
                            .findFirst()
                            .orElse(null);
                }
                case DUENO -> {
                    Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
                    ingrediente = ingredienteService.getIngredientesByDuenoId(currDueno.getId()).stream()
                            .filter(ing -> ing.getId().equals(id))
                            .findFirst()
                            .orElse(null);
                }
                default -> {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }
            IngredienteDTO ingredienteDTO = ingredienteService.convertirIngredienteToDTO(ingrediente);
            return new ResponseEntity<>(ingredienteDTO, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<Ingrediente>> findByCantidad(@PathVariable("cantidad") Integer cantidad) {
        User user = userService.findCurrentUser();
        List<Ingrediente> ingredientes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> ingredientes = ingredienteService.getIngredientesByCantidad(cantidad);
            case EMPLEADO -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId()).stream()
                        .filter(ing -> ing.getCantidad().equals(cantidad))
                        .toList();
            }
            case DUENO -> {
                Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByDuenoId(currDueno.getId()).stream()
                        .filter(ing -> ing.getCantidad().equals(cantidad))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @GetMapping("/productoInventario/{productoInventario}")
    public ResponseEntity<List<Ingrediente>> findByProductoInventarioId(@PathVariable("productoInventario") Integer productoInventario) {
        User user = userService.findCurrentUser();
        List<Ingrediente> ingredientes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> ingredientes = ingredienteService.getIngredientesByProductoInventarioId(productoInventario);
            case EMPLEADO -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId()).stream()
                        .filter(ing -> ing.getProductoInventario().getId().equals(productoInventario))
                        .toList();
            }
            case DUENO -> {
                Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByDuenoId(currDueno.getId()).stream()
                        .filter(ing -> ing.getProductoInventario().getId().equals(productoInventario))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @GetMapping("/productoVenta/{productoVenta}")
    public ResponseEntity<List<Ingrediente>> findByProductoVentaId(@PathVariable("productoVenta") Integer productoVenta) {
        User user = userService.findCurrentUser();
        List<Ingrediente> ingredientes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> ingredientes = ingredienteService.getIngredientesByProductoVentaId(productoVenta);
            case EMPLEADO -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId()).stream()
                        .filter(ing -> ing.getProductoVenta().getId().equals(productoVenta))
                        .toList();
            }
            case DUENO -> {
                Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByDuenoId(currDueno.getId()).stream()
                        .filter(ing -> ing.getProductoVenta().getId().equals(productoVenta))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Ingrediente>> findByNegocioId(@PathVariable("negocio") Integer negocio) {
        User user = userService.findCurrentUser();
        List<Ingrediente> ingredientes;
        switch (user.getAuthority().getAuthority()){
            case ADMIN -> ingredientes = ingredienteService.getIngredientesByNegocioId(negocio);
            case EMPLEADO -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByNegocioId(currEmpleado.getNegocio().getId()).stream()
                        .filter(ing -> ing.getProductoInventario().getCategoria().getNegocio().getId().equals(negocio))
                        .toList();
            }
            case DUENO -> {
                Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
                ingredientes = ingredienteService.getIngredientesByDuenoId(currDueno.getId()).stream()
                        .filter(ing -> ing.getProductoInventario().getCategoria().getNegocio().getId().equals(negocio))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(ingredientes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ingrediente> save(@RequestBody @Valid Ingrediente ingrediente) {
        User user = userService.findCurrentUser();
        Negocio negocio = productoInventarioService.getById(ingrediente.getProductoInventario().getId()).getCategoria().getNegocio();

        if(! ((user.getAuthority().getAuthority().equals(ADMIN)) ||
                (user.getAuthority().getAuthority().equals(DUENO)) && negocio.getDueno().getUser().getId().equals(user.getId()) ||
                (user.getAuthority().getAuthority().equals(EMPLEADO) && empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId())))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(ingredienteService.save(ingrediente), HttpStatus.CREATED);
    }

    @PostMapping("/dto")
    public ResponseEntity<Ingrediente> save(@RequestBody @Valid IngredienteDTO ingrediente) {
        User user = userService.findCurrentUser();
        ProductoInventario piIngrediente = productoInventarioService.getById(ingrediente.getProductoInventarioId());
        Negocio negocio = piIngrediente.getCategoria().getNegocio();

        if(! ((user.getAuthority().getAuthority().equals(ADMIN)) ||
                (user.getAuthority().getAuthority().equals(DUENO)) && negocio.getDueno().getUser().getId().equals(user.getId()) ||
                (user.getAuthority().getAuthority().equals(EMPLEADO) && empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId())))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Ingrediente ingredienteToSave = ingredienteService.convertirDTOToIngrediente(ingrediente);
        return new ResponseEntity<>(ingredienteService.save(ingredienteToSave), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingrediente> update(@PathVariable("id") Integer id, @RequestBody @Valid Ingrediente ingrediente) {
        try {
            User user = userService.findCurrentUser();
            Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
            Ingrediente existingIngrediente = ingredienteService.getById(id);
            
            if (existingIngrediente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Negocio negocio = productoInventarioService.getById(ingrediente.getProductoInventario().getId()).getCategoria().getNegocio();
            Negocio negocioToUpdate = existingIngrediente.getProductoInventario().getCategoria().getNegocio();

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

            ingrediente.setId(id);
            return new ResponseEntity<>(ingredienteService.save(ingrediente), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<Ingrediente> update(@PathVariable("id") Integer id, @RequestBody @Valid IngredienteDTO ingrediente) {
        try {
            User user = userService.findCurrentUser();
            Dueno currDueno =  duenoService.getDuenoByUser(user.getId());
            Ingrediente existingIngrediente = ingredienteService.getById(id);

            if (existingIngrediente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Negocio negocio = productoInventarioService.getById(ingrediente.getProductoInventarioId()).getCategoria().getNegocio();
            Negocio negocioToUpdate = existingIngrediente.getProductoInventario().getCategoria().getNegocio();

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
            Ingrediente ingredienteToUpdate = ingredienteService.convertirDTOToIngrediente(ingrediente);
            ingredienteToUpdate.setId(id);
            return new ResponseEntity<>(ingredienteService.save(ingredienteToUpdate), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        try {
            User user = userService.findCurrentUser();
            Ingrediente ingrediente = ingredienteService.getById(id);
            if (ingrediente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Negocio negocio = productoInventarioService.getById(ingrediente.getProductoInventario().getId()).getCategoria().getNegocio();

            if(!((user.getAuthority().getAuthority().equals(ADMIN)) ||
                    (user.getAuthority().getAuthority().equals(DUENO) &&
                            user.getId().equals(negocio.getDueno().getUser().getId())) ||
                    (user.getAuthority().getAuthority().equals(EMPLEADO) &&
                            empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId()))))  {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            ingredienteService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
