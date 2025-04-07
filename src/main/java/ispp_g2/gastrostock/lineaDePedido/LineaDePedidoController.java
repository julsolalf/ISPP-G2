package ispp_g2.gastrostock.lineaDePedido;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.pedido.PedidoService;
import ispp_g2.gastrostock.productoVenta.ProductoVentaService;

import java.util.List;

@RestController
@RequestMapping("/api/lineasDePedido")
public class LineaDePedidoController {

//   TO DO: Implementar la logica de que sololos usuarios del restaurante pueden ver sus lineas de pedido
    private final LineaDePedidoService lineaDePedidoService;

    @Autowired
    public LineaDePedidoController(LineaDePedidoService lineaDePedidoService, PedidoService pedidoService, ProductoVentaService productoVentaService) {
        this.lineaDePedidoService = lineaDePedidoService;
    }

    @GetMapping
    public ResponseEntity<List<LineaDePedido>> findAll() {
        if (lineaDePedidoService.getLineasDePedido().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lineaDePedidoService.getLineasDePedido(),
                HttpStatus.OK);

    }

    @GetMapping("/dto")
    public ResponseEntity<List<LineaDePedidoDTO>> findAllDto() {
        List<LineaDePedido> lineasDePedido= lineaDePedidoService.getLineasDePedido();
        if (lineasDePedido.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lineasDePedido.stream().map(LineaDePedidoDTO::of).toList(), HttpStatus.OK);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaDePedido> findById(@PathVariable("id") Integer id) {
        LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
        if (lineaDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineaDePedido, HttpStatus.OK);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<LineaDePedidoDTO> findByIdDto(@PathVariable("id") Integer id) {
        LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
        if (lineaDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedido), HttpStatus.OK);
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

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<LineaDePedido>> findByPedidoId(@PathVariable("pedidoId") Integer pedidoId) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lineasDePedido, HttpStatus.OK);
    }

    @GetMapping("/dto/pedido/{pedidoId}")
    public ResponseEntity<List<LineaDePedidoDTO>> findByPedidoIdDto(@PathVariable("pedidoId") Integer pedidoId) {
        List<LineaDePedido> lineasDePedido = lineaDePedidoService.getLineasDePedidoByPedidoId(pedidoId);
        if (lineasDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(lineasDePedido.stream().map(LineaDePedidoDTO::of).toList(), HttpStatus.OK);
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
    public ResponseEntity<LineaDePedidoDTO> save(@RequestBody @Valid LineaDePedidoDTO lineaDePedidoDTO) {
        if (lineaDePedidoDTO == null) {
            throw new IllegalArgumentException("Linea de pedido no puede ser nula");
        }
        LineaDePedidoDTO res = LineaDePedidoDTO.of(lineaDePedidoService.save(lineaDePedidoService.convertDtoLineaDePedido(lineaDePedidoDTO)));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaDePedidoDTO> update(@PathVariable("id") Integer id, @RequestBody @Valid LineaDePedidoDTO lineaDePedidoDTO) {
        if (lineaDePedidoDTO == null) {
            throw new IllegalArgumentException("Linea de pedido no puede ser nula");
        }
        LineaDePedido toUpdate = lineaDePedidoService.getById(id);
        if (toUpdate == null) { 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BeanUtils.copyProperties(lineaDePedidoService.convertDtoLineaDePedido(lineaDePedidoDTO), toUpdate,"id");
        return new ResponseEntity<>(LineaDePedidoDTO.of(lineaDePedidoService.save(toUpdate)), HttpStatus.OK);
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        LineaDePedido lineaDePedido = lineaDePedidoService.getById(id);
        if (lineaDePedido == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lineaDePedidoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
