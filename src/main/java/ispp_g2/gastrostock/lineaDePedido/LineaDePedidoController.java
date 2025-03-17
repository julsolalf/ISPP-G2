package ispp_g2.gastrostock.lineaDePedido;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lineasDePedido")
public class LineaDePedidoController {

//   TO DO: Implementar la logica de que sololos usuarios del restaurante pueden ver sus lineas de pedido
    private LineaDePedidoService lineaDePedidoService;

    @Autowired
    public LineaDePedidoController(LineaDePedidoService lineaDePedidoService) {
        this.lineaDePedidoService = lineaDePedidoService;
    }

    @GetMapping
    public ResponseEntity<List<LineaDePedido>> findAll() {
        if (lineaDePedidoService.getLineasDePedido().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>((List<LineaDePedido>) lineaDePedidoService.getLineasDePedido(),
                HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaDePedido> findById(@PathVariable("id") String id) {
        LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
        if (lineaDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineaDePedido, HttpStatus.OK);
    }

    @GetMapping("/cantidad/{cantidad}")
    public ResponseEntity<List<LineaDePedido>> findByCantidad(@PathVariable("cantidad") Integer cantidad) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByCantidad(cantidad);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
    }

    @GetMapping("/precioLinea/{precioLinea}")
    public ResponseEntity<List<LineaDePedido>> findByPrecioLinea(@PathVariable("precioLinea") Double precioLinea) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPrecioLinea(precioLinea);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
    }

    @GetMapping("/pedido/{pedido}")
    public ResponseEntity<List<LineaDePedido>> findByPedidoId(@PathVariable("pedido") Integer pedido) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedido);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
    }

    @GetMapping("/producto/{producto}")
    public ResponseEntity<List<LineaDePedido>> findByProductoId(@PathVariable("producto") Integer producto) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByProductoId(producto);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
    }

    @GetMapping("/producto/{producto}/cantidad/{cantidad}")
    public ResponseEntity<List<LineaDePedido>> findByProductoIdAndCantidad(@PathVariable("producto") Integer producto, @PathVariable("cantidad") Integer cantidad) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByProductoIdAndCantidad(producto, cantidad);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
    }

    @GetMapping("/producto/{producto}/precioLinea/{precioLinea}")
    public ResponseEntity<List<LineaDePedido>> findByProductoIdAndPrecioLinea(@PathVariable("producto") Integer producto, @PathVariable("precioLinea") Double precioLinea) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByProductoIdAndPrecioLinea(producto, precioLinea);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LineaDePedido> save(@RequestBody @Valid LineaDePedido lineaDePedido) {
        if (lineaDePedido == null) {
            throw new IllegalArgumentException("Linea de pedido no puede ser nula");
        }
        return new ResponseEntity<>(lineaDePedidoService.save(lineaDePedido), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaDePedido> update(@PathVariable("id") String id, @RequestBody @Valid LineaDePedido lineaDePedido) {
        if (lineaDePedido == null) {
            throw new IllegalArgumentException("Linea de pedido no puede ser nula");
        }
        if (lineaDePedidoService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lineaDePedido.setId(Integer.valueOf(id));
        return new ResponseEntity<>(lineaDePedidoService.save(lineaDePedido), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
        if (lineaDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lineaDePedidoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
