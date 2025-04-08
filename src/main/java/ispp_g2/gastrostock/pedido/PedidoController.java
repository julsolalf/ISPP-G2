package ispp_g2.gastrostock.pedido;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.mesa.MesaService;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UserService userService;
    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;
    private final MesaService mesaService;
    private final NegocioService negocioService;

    private static final String ADMIN = "admin";
    private static final String EMPLEADO = "empleado";
    private static final String DUENO = "dueno";

    @Autowired
    public PedidoController(PedidoService pedidoService, UserService userService, DuenoService duenoService,
            EmpleadoService empleadoService,
            MesaService mesaService, NegocioService negocioService) {
        this.pedidoService = pedidoService;
        this.userService = userService;
        this.duenoService = duenoService;
        this.empleadoService = empleadoService;
        this.mesaService = mesaService;
        this.negocioService = negocioService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> findAll() {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getAll();
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(pedidoService.getById(id), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Pedido pedido = pedidoService.getById(id);
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedido.getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(pedido, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Pedido pedido = pedidoService.getById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (pedido.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(pedido, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<PedidoDto> findByIdDto(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(pedidoService.convertirPedidoDto(pedidoService.getById(id)), HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            Pedido pedido = pedidoService.getById(id);
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedido.getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(pedidoService.convertirPedidoDto(pedido), HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Pedido pedido = pedidoService.getById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (pedido.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(pedidoService.convertirPedidoDto(pedido), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Pedido>> findByFecha(@PathVariable("fecha") LocalDateTime fecha) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByFecha(fecha);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/precioTotal/{precioTotal}")
    public ResponseEntity<List<Pedido>> findByPrecioTotal(@PathVariable("precioTotal") Double precioTotal) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByPrecioTotal(precioTotal);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/mesa/{mesa}")
    public ResponseEntity<List<Pedido>> findByMesaId(@PathVariable("mesa") Integer mesa) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByMesaId(mesa);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (mesaService.getById(mesa).getNegocio().getDueno().getId()
                    .equals(duenoService.getDuenoByUser(user.getId()).getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByMesaId(mesa);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos, HttpStatus.OK);
            }

        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            if (mesaService.getById(mesa).getNegocio().getId()
                    .equals(empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByMesaId(mesa);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/mesa/{mesa}")
    public ResponseEntity<List<PedidoDto>> findByMesaIdDto(@PathVariable("mesa") Integer mesa) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByMesaId(mesa);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                    HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (mesaService.getById(mesa).getNegocio().getDueno().getId()
                    .equals(duenoService.getDuenoByUser(user.getId()).getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByMesaId(mesa);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                        HttpStatus.OK);
            }

        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            if (mesaService.getById(mesa).getNegocio().getId()
                    .equals(empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByMesaId(mesa);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/empleado/{empleado}")
    public ResponseEntity<List<Pedido>> findByEmpleadoId(@PathVariable("empleado") Integer empleado) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByEmpleadoId(empleado);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getDueno().getId()
                    .equals(duenoService.getDuenoByUser(user.getId()).getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByEmpleadoId(empleado);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            if (empleadoService.getEmpleadoByUser(user.getId()).getId().equals(empleado)) {
                List<Pedido> pedidos = pedidoService.getPedidoByEmpleadoId(empleado);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/empleado/{empleado}")
    public ResponseEntity<List<PedidoDto>> findByEmpleadoIdDto(@PathVariable("empleado") Integer empleado) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByEmpleadoId(empleado);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                    HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getDueno().getId()
                    .equals(duenoService.getDuenoByUser(user.getId()).getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByEmpleadoId(empleado);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                        HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            if (empleadoService.getEmpleadoByUser(user.getId()).getId().equals(empleado)) {
                List<Pedido> pedidos = pedidoService.getPedidoByEmpleadoId(empleado);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/venta/{venta}")
    public ResponseEntity<List<Pedido>> findByNegocioId(@PathVariable("venta") Integer negocio) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByNegocioId(negocio);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (negocioService.getById(negocio).getDueno().getId()
                    .equals(duenoService.getDuenoByUser(user.getId()).getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByNegocioId(negocio);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos, HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            if (negocioService.getById(negocio).getId()
                    .equals(empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByNegocioId(negocio);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/venta/{venta}")
    public ResponseEntity<List<PedidoDto>> findByNegocioIdDto(@PathVariable("venta") Integer negocio) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(ADMIN).equals(true)) {
            List<Pedido> pedidos = pedidoService.getPedidoByNegocioId(negocio);
            if (pedidos == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                    HttpStatus.OK);
        } else if (user.hasAnyAuthority(DUENO).equals(true)) {
            if (negocioService.getById(negocio).getDueno().getId()
                    .equals(duenoService.getDuenoByUser(user.getId()).getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByNegocioId(negocio);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                        HttpStatus.OK);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            if (negocioService.getById(negocio).getId()
                    .equals(empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId())) {
                List<Pedido> pedidos = pedidoService.getPedidoByNegocioId(negocio);
                if (pedidos == null) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(pedidos.stream().map(pedidoService::convertirPedidoDto).toList(),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private void isPedidoValid(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no puede ser nulo");
        }
        if (!pedido.getEmpleado().getNegocio().getId().equals(pedido.getNegocio().getId())) {
            throw new IllegalArgumentException("El empleado no pertenece al negocio");
        }
        if (!pedido.getMesa().getNegocio().getId().equals(pedido.getNegocio().getId())) {
            throw new IllegalArgumentException("La mesa no pertenece al negocio");
        }
    }

    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody @Valid Pedido pedido) {
        User user = userService.findCurrentUser();
        isPedidoValid(pedido);
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedido.getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(pedidoService.save(pedido), HttpStatus.CREATED);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            pedido.setEmpleado(empleado);
            if (pedido.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(pedidoService.save(pedido), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/dto")
    public ResponseEntity<Pedido> saveDto(@RequestBody @Valid PedidoDto pedidoDto) {
        User user = userService.findCurrentUser();
        if (pedidoDto == null) {
            throw new IllegalArgumentException("Pedido no puede ser nulo");
        }
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedidoDto.getEmpleadoId() == null) {
                throw new IllegalArgumentException("Empleado no puede ser nulo");
            }
            Pedido pedido = pedidoService.convertirPedido(pedidoDto);
            isPedidoValid(pedido);
            if (pedido.getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(pedidoService.save(pedido), HttpStatus.CREATED);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            pedidoDto.setEmpleadoId(empleado.getId());
            Pedido pedido = pedidoService.convertirPedido(pedidoDto);
            isPedidoValid(pedido);
            if (pedido.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(pedidoService.save(pedido), HttpStatus.CREATED);
            }

        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable("id") Integer id, @RequestBody @Valid Pedido pedido) {
        User user = userService.findCurrentUser();
        isPedidoValid(pedido);
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedido.getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(pedidoService.update(id, pedido), HttpStatus.OK);
            }

        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (pedido.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(pedidoService.update(id, pedido), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<Pedido> updateDto(@PathVariable("id") Integer id, @RequestBody @Valid PedidoDto pedidoDto) {
        User user = userService.findCurrentUser();
        if (pedidoDto == null) {
            throw new IllegalArgumentException("Pedido no puede ser nulo");
        }
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedidoDto.getEmpleadoId() == null) {
                throw new IllegalArgumentException("Empleado no puede ser nulo");
            }
            Pedido pedido = pedidoService.convertirPedido(pedidoDto);
            isPedidoValid(pedido);
            if (pedido.getNegocio().getDueno().getId().equals(dueno.getId())) {
                return new ResponseEntity<>(pedidoService.update(id, pedido), HttpStatus.OK);
            }

        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            pedidoDto.setEmpleadoId(empleado.getId());
            Pedido pedido = pedidoService.convertirPedido(pedidoDto);
            isPedidoValid(pedido);
            if (pedido.getNegocio().getId().equals(empleado.getNegocio().getId())) {
                return new ResponseEntity<>(pedidoService.update(id, pedido), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if (user.hasAnyAuthority(DUENO).equals(true)) {
            Dueno dueno = duenoService.getDuenoByUser(user.getId());
            if (pedidoService.getById(id).getNegocio().getDueno().getId().equals(dueno.getId())) {
                pedidoService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else if (user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if (pedidoService.getById(id).getNegocio().getId().equals(empleado.getNegocio().getId())) {
                pedidoService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
