package ispp_g2.gastrostock.pedido;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> findAll() {
        if (pedidoService.getAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pedidoService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable("id") String id) {
        Pedido pedido = pedidoService.getById(id);
        if (pedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Pedido>> findByFecha(@PathVariable("fecha") LocalDateTime fecha) {
        List<Pedido> pedidos = pedidoService.getPedidoByFecha(fecha);
        if (pedidos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/precioTotal/{precioTotal}")
    public ResponseEntity<List<Pedido>> findByPrecioTotal(@PathVariable("precioTotal") Double precioTotal) {
        List<Pedido> pedidos = pedidoService.getPedidoByPrecioTotal(precioTotal);
        if (pedidos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/mesa/{mesa}")
    public ResponseEntity<List<Pedido>> findByMesaId(@PathVariable("mesa") Integer mesa) {
        List<Pedido> pedidos = pedidoService.getPedidoByMesaId(mesa);
        if (pedidos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/empleado/{empleado}")
    public ResponseEntity<List<Pedido>> findByEmpleadoId(@PathVariable("empleado") Integer empleado) {
        List<Pedido> pedidos = pedidoService.getPedidoByEmpleadoId(empleado);
        if (pedidos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Pedido>> findByNegocioId(@PathVariable("negocio") Integer negocio) {
        List<Pedido> pedidos = pedidoService.getPedidoByNegocioId(negocio);
        if (pedidos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody @Valid Pedido pedido) {
        if(pedido == null){
            throw new IllegalArgumentException("Pedido no puede ser nulo");
        }
        return new ResponseEntity<>(pedidoService.save(pedido), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable("id") String id, @RequestBody @Valid Pedido pedido) {
        if(pedido == null){
            throw new IllegalArgumentException("Pedido no puede ser nulo");
        }
        if(pedido.getId() == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        pedido.setId(Integer.valueOf(id));
        return new ResponseEntity<>(pedidoService.save(pedido), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        pedidoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
