package ispp_g2.gastrostock.lineaDePedido;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.ingrediente.IngredienteService;
import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.lote.LoteService;
import ispp_g2.gastrostock.pedido.PedidoService;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoVenta.ProductoVentaService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lineasDePedido")
public class LineaDePedidoController {

    private final LineaDePedidoService lineaDePedidoService;
    private final PedidoService pedidoService;
    private final UserService userService;
    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;
    private final IngredienteService ingredienteService;
    private final LoteService loteService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @Autowired
    public LineaDePedidoController(LineaDePedidoService lineaDePedidoService, PedidoService pedidoService,
            UserService userService, DuenoService duenoService,
            EmpleadoService empleadoService, IngredienteService ingredienteService, LoteService loteService) {
        this.lineaDePedidoService = lineaDePedidoService;
        this.pedidoService = pedidoService;
        this.userService = userService;
        this.duenoService = duenoService;
        this.empleadoService = empleadoService;
        this.ingredienteService = ingredienteService;
        this.loteService = loteService;
    }

    @GetMapping
    public ResponseEntity<List<LineaDePedido>> findAll() {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedido();
            if (lineasDePedido.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedido();
            lineasDePedido = lineasDePedido
                    .stream()
                    .filter(lineaDePedido -> lineaDePedido.getPedido().getMesa().getNegocio().getDueno()
                            .getId().equals(dueno.getId()))
                    .toList();
            if (lineasDePedido.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedido();
            lineasDePedido = lineasDePedido
                    .stream()
                    .filter(lineaDePedido -> lineaDePedido.getPedido().getMesa().getNegocio()
                            .getId().equals(empleado.getNegocio().getId()))
                    .toList();
            if (lineasDePedido.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping("/dto")
    public ResponseEntity<List<LineaDePedidoDTO>> findAllDto() {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedidoDTO> lineasDePedido = lineaDePedidoService.getLineasDePedido()
                    .stream()
                    .map(LineaDePedidoDTO::of).toList();
            if (lineasDePedido.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            List<LineaDePedidoDTO> lineasDePedido = lineaDePedidoService.getLineasDePedido()
                    .stream()
                    .filter(lineaDePedido -> lineaDePedido.getPedido().getMesa().getNegocio().getDueno()
                            .getId().equals(dueno.getId()))
                    .map(LineaDePedidoDTO::of).toList();
            if (lineasDePedido.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            List<LineaDePedidoDTO> lineasDePedido = lineaDePedidoService.getLineasDePedido()
                    .stream()
                    .filter(lineaDePedido -> lineaDePedido.getPedido().getMesa().getNegocio()
                            .getId().equals(empleado.getNegocio().getId()))
                    .map(LineaDePedidoDTO::of).toList();
            if (lineasDePedido.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaDePedido> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(lineaDePedidoService.getById(id), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(lineaDePedido, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(lineaDePedido, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<LineaDePedidoDTO> findByIdDto(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.getById(id)), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedido), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedido), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<LineaDePedido>> findByCantidad(@PathVariable("cantidad") Integer cantidad) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByCantidad(cantidad);
            if (lineasDePedido == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/precioLinea/{precioLinea}")
    public ResponseEntity<List<LineaDePedido>> findByPrecioLinea(@PathVariable("precioLinea") Double precioLinea) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPrecioLinea(precioLinea);
            if (lineasDePedido == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<LineaDePedido>> findByPedidoId(@PathVariable("pedidoId") Integer pedidoId) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
            if (lineasDePedido == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedidoService.getById(pedidoId).getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
                if (lineasDePedido == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (pedidoService.getById(pedidoId).getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
                if (lineasDePedido == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/pedido/{pedidoId}")
    public ResponseEntity<List<LineaDePedidoDTO>> findByPedidoIdDto(@PathVariable("pedidoId") Integer pedidoId) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
            if (lineasDePedido == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineasDePedido.stream().map(LineaDePedidoDTO::of).toList(), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedidoService.getById(pedidoId).getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
                if (lineasDePedido == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineasDePedido
                        .stream().map(LineaDePedidoDTO::of).toList(), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (pedidoService.getById(pedidoId).getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
                if (lineasDePedido == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(lineasDePedido
                        .stream().map(LineaDePedidoDTO::of).toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/producto/{producto}")
    public ResponseEntity<List<LineaDePedido>> findByProductoId(@PathVariable("producto") Integer producto) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByProductoId(producto);
            if (lineasDePedido == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/producto/{producto}/cantidad/{cantidad}")
    public ResponseEntity<List<LineaDePedido>> findByProductoIdAndCantidad(@PathVariable("producto") Integer producto,
            @PathVariable("cantidad") Integer cantidad) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByProductoIdAndCantidad(producto,
                    cantidad);
            if (lineasDePedido == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/producto/{producto}/precioUnitario/{precioUnitario}")
    public ResponseEntity<List<LineaDePedido>> findByProductoIdAndPrecioUnitario(
            @PathVariable("producto") Integer producto, @PathVariable("precioUnitario") Double precioUnitario) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<LineaDePedido> lineasDePedido = lineaDePedidoService
                    .getLineasDePedidoByProductoIdAndPrecioUnitario(producto, precioUnitario);
            if (lineasDePedido == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private void isLineaDePedidoValid(LineaDePedido lineaDePedido) {
        if (lineaDePedido == null) {
            throw new IllegalArgumentException("Linea de pedido no puede ser nula");
        }
        if (!lineaDePedido.getPedido().getMesa().getNegocio().getId().equals(
                lineaDePedido.getProducto().getCategoria().getNegocio().getId())) {
            throw new IllegalArgumentException("Linea de pedido no pertenece al mismo negocio");
        }
        List<Ingrediente> ingredientes = ingredienteService
                .getIngredientesByProductoVentaId(lineaDePedido.getProducto().getId());
        for (Ingrediente ingrediente : ingredientes) {
            ProductoInventario productoInventario = ingrediente.getProductoInventario();
            List<Lote> lotes = loteService.getLotesByProductoId(productoInventario.getId()).stream()
                    .filter(lote -> lote.getFechaCaducidad().isAfter(LocalDate.now())).toList();
            if (productoInventario.calcularCantidad(lotes) < ingrediente.getCantidad() * lineaDePedido.getCantidad()) {
                throw new IllegalArgumentException("No hay suficiente stock de " + productoInventario.getName());
            }
        }
    }

    @PostMapping
    public ResponseEntity<LineaDePedidoDTO> save(@RequestBody @Valid LineaDePedidoDTO lineaDePedidoDTO) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.convertDtoLineaDePedido(lineaDePedidoDTO);
            lineaDePedido.setEstado(false);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                isLineaDePedidoValid(lineaDePedido);
                return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.save(lineaDePedido)),
                        HttpStatus.CREATED);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.convertDtoLineaDePedido(lineaDePedidoDTO);
            lineaDePedido.setEstado(false);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                isLineaDePedidoValid(lineaDePedido);
                return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.save(lineaDePedido)),
                        HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaDePedidoDTO> update(@PathVariable("id") Integer id,
            @RequestBody @Valid LineaDePedidoDTO lineaDePedidoDTO) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            LineaDePedido toUpdate = lineaDePedidoService.getById(id);
            if (toUpdate.getPedido().getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                LineaDePedido lineaDePedido = lineaDePedidoService.convertDtoLineaDePedido(lineaDePedidoDTO);
                if (lineaDePedido.getPedido().getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                    isLineaDePedidoValid(lineaDePedido);
                    return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.update(id, lineaDePedido)),
                            HttpStatus.OK);
                }
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            LineaDePedido toUpdate = lineaDePedidoService.getById(id);
            if (toUpdate.getPedido().getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                LineaDePedido lineaDePedido = lineaDePedidoService.convertDtoLineaDePedido(lineaDePedidoDTO);
                if (lineaDePedido.getPedido().getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                    isLineaDePedidoValid(lineaDePedido);
                    return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.update(id, lineaDePedido)),
                            HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<LineaDePedidoDTO> updateEstado(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.cambiarEstado(id)), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
            if (lineaDePedido.getPedido().getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                isLineaDePedidoValid(lineaDePedido);
                return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.cambiarEstado(id)), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        LineaDePedido toDelete = lineaDePedidoService.getById(id);
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (toDelete.getPedido().getMesa().getNegocio().getDueno().getId().equals(dueno.getId())) {
                lineaDePedidoService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (toDelete.getPedido().getMesa().getNegocio().getId().equals(empleado.getNegocio().getId())) {
                lineaDePedidoService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
