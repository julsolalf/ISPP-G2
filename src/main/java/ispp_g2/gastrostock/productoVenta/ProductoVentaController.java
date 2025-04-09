package ispp_g2.gastrostock.productoVenta;

import ispp_g2.gastrostock.categorias.CategoriaService;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productosVenta")
public class ProductoVentaController {

    private final ProductoVentaService productoVentaService;
    private final UserService userService;

    private final String admin = "admin";
    private final String empleado = "empleado";
    private final String dueno = "dueno";
    private final EmpleadoService empleadoService;
    private final DuenoService duenoService;
    private final NegocioService negocioService;
    private final CategoriaService categoriaService;

    @Autowired
    public ProductoVentaController(ProductoVentaService productoVentaService, UserService userService, EmpleadoService empleadoService, DuenoService duenoService, NegocioService negocioService, CategoriaService categoriaService) {
        this.productoVentaService = productoVentaService;
        this.userService = userService;
        this.empleadoService = empleadoService;
        this.duenoService = duenoService;
        this.negocioService = negocioService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoVenta>> findAll() {
        User user = userService.findCurrentUser();
        List<ProductoVenta> productosVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productosVenta = productoVentaService.getProductosVenta();
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId());
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productoVentaService.getProductosVenta().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productosVenta,
                HttpStatus.OK);

    }

    @GetMapping("/dto")
    public ResponseEntity<List<ProductoVentaDTO>> findAllDTO() {
        User user = userService.findCurrentUser();
        List<ProductoVenta> productosVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productosVenta = productoVentaService.getProductosVenta();
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId());
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId());
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productosVenta.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<ProductoVentaDTO> productosDTO = new ArrayList<>();
        for (ProductoVenta producto : productosVenta) {
            productosDTO.add(productoVentaService.convertirProductoVentaDTO(producto));
        }
        return new ResponseEntity<>(productosDTO, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoVenta> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        ProductoVenta productoVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productoVenta = productoVentaService.getById(id);
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productoVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId()).stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productoVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId()).stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productoVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productoVenta, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<ProductoVentaDTO> findByIdDTO(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        ProductoVenta productoVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productoVenta = productoVentaService.getById(id);
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productoVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId()).stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productoVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId()).stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productoVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productoVentaService.convertirProductoVentaDTO(productoVenta), HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ProductoVenta>> findByNombre(@PathVariable("nombre") String nombre) {
        User user = userService.findCurrentUser();
        List<ProductoVenta> productosVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productosVenta = productoVentaService.getProductosVentaByNombre(nombre);
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId()).stream()
                        .filter(p -> p.getName().equalsIgnoreCase(nombre))
                        .toList();
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId()).stream()
                        .filter(p -> p.getName().equalsIgnoreCase(nombre))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/categoriaVenta/{categoriaVenta}")
    public ResponseEntity<List<ProductoVenta>> findByCategoriaVenta(@PathVariable("categoriaVenta") String categoriaVenta) {
        User user = userService.findCurrentUser();
        List<ProductoVenta> productosVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productosVenta = productoVentaService.getProductosVentaByCategoriaVenta(categoriaVenta);
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId()).stream()
                        .filter(p -> p.getCategoria().getName().equalsIgnoreCase(categoriaVenta))
                        .toList();
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId()).stream()
                        .filter(p -> p.getCategoria().getName().equalsIgnoreCase(categoriaVenta))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/precioVenta/{precioVenta}")
    public ResponseEntity<List<ProductoVenta>> findByPrecioVenta(@PathVariable("precioVenta") Double precioVenta) {
        User user = userService.findCurrentUser();
        List<ProductoVenta> productosVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productosVenta = productoVentaService.getProductosVentaByPrecioVenta(precioVenta);
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId()).stream()
                        .filter(p -> p.getPrecioVenta().equals(precioVenta))
                        .toList();
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId()).stream()
                        .filter(p -> p.getPrecioVenta().equals(precioVenta))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/categoriaVenta/{categoriaVenta}/precioVenta/{precioVenta}")
    public ResponseEntity<List<ProductoVenta>> findByCategoriaVentaAndPrecioVenta(@PathVariable("categoriaVenta") String categoriaVenta, @PathVariable("precioVenta") Double precioVenta) {
        User user = userService.findCurrentUser();
        List<ProductoVenta> productosVenta;
        switch (user.getAuthority().getAuthority()) {
            case admin -> productosVenta = productoVentaService.getProductosVentaByCategoriaVentaAndPrecioVenta(categoriaVenta, precioVenta);
            case empleado -> {
                Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByNegocioID(currEmpleado.getNegocio().getId()).stream()
                        .filter(p -> p.getCategoria().getName().equalsIgnoreCase(categoriaVenta) && p.getPrecioVenta().equals(precioVenta))
                        .toList();
            }
            case dueno -> {
                Dueno currDueno = duenoService.getDuenoByUser(user.getId());
                productosVenta = productoVentaService.getProductosVentaByDuenoID(currDueno.getId()).stream()
                        .filter(p -> p.getCategoria().getName().equalsIgnoreCase(categoriaVenta) && p.getPrecioVenta().equals(precioVenta))
                        .toList();
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductoVenta> save(@RequestBody @Valid ProductoVentaDTO productoVenta) {
        User user = userService.findCurrentUser();

        if (productoVenta == null) {
            throw new IllegalArgumentException("Producto de venta no puede ser nulo");
        }

        Negocio negocio = categoriaService.getById(productoVenta.getCategoriaId()).getNegocio();
//        Si no es admin
        if(!((user.getAuthority().getAuthority().equals(admin)) ||
//                Ni dueno del negocio al que pertence la categoria del producto
                (user.getAuthority().getAuthority().equals(dueno) &&
                        user.getId().equals(negocio.getDueno().getUser().getId())) ||
//                Ni empleado del negocio al que pertence la categoria del producto
                (user.getAuthority().getAuthority().equals(empleado) &&
                        empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId())))) {

            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

        ProductoVenta productoVentaEntity = productoVentaService.convertirDTOProductoVenta(productoVenta);
        return new ResponseEntity<>(productoVentaService.save(productoVentaEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoVenta> update(@PathVariable("id") Integer id, @RequestBody @Valid ProductoVentaDTO productoVenta) {
        User user = userService.findCurrentUser();
        Dueno currDueno = duenoService.getDuenoByUser(user.getId());
        ProductoVenta productoVentaToUpdate = productoVentaService.getById(id);

        if (productoVenta == null) {
            throw new IllegalArgumentException("Producto de venta no puede ser nulo");
        }
        if(productoVentaToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Negocio negocio = categoriaService.getById(productoVenta.getCategoriaId()).getNegocio();
        Negocio negocioToUpdate = productoVentaToUpdate.getCategoria().getNegocio();

//        Si no es admin
        if(! ((user.getAuthority().getAuthority().equals(admin)) ||
//                Ni dueno del negocio al que pertence la categoria del producto ni cambia a una categoria de otro negocio que no le pertenece
                (user.getAuthority().getAuthority().equals(dueno)  &&
                        currDueno.getId().equals(negocioToUpdate.getDueno().getId()) &&
                        negocioService.getByDueno(currDueno.getId()).contains(negocio)) ||
//                Ni empleado del negocio al que pertence la categoria del producto ni cambia a una categoria de otro negocio
                (user.getAuthority().getAuthority().equals(empleado) &&
                        empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocioToUpdate.getId()) &&
                        negocio.equals(negocioToUpdate)))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ProductoVenta productoVentaEntity = productoVentaService.convertirDTOProductoVenta(productoVenta);
        productoVentaEntity.setId(id);
        return new ResponseEntity<>(productoVentaService.save(productoVentaEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        ProductoVenta productoVenta = productoVentaService.getById(id);

        if (productoVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Negocio negocio = productoVenta.getCategoria().getNegocio();

        if(!((user.getAuthority().getAuthority().equals(admin)) ||
                (user.getAuthority().getAuthority().equals(dueno) &&
                        user.getId().equals(negocio.getDueno().getUser().getId())) ||
                (user.getAuthority().getAuthority().equals(empleado) &&
                        empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId()))))  {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        productoVentaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
