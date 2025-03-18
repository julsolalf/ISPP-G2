package ispp_g2.gastrostock.productoVenta;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productoVenta")
public class ProductoVentaController {

    private ProductoVentaService productoVentaService;

    @Autowired
    public ProductoVentaController(ProductoVentaService productoVentaService) {
        this.productoVentaService = productoVentaService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoVenta>> findAll() {
        if (productoVentaService.getProductosVenta().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>((List<ProductoVenta>) productoVentaService.getProductosVenta(),
                HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoVenta> findById(@PathVariable("id") String id) {
        ProductoVenta productoVenta = productoVentaService.getById(id);
        if (productoVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productoVenta, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ProductoVenta>> findByNombre(@PathVariable("nombre") String nombre) {
        List<ProductoVenta> productosVenta = productoVentaService.getProductosVentaByNombre(nombre);
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/categoriaVenta/{categoriaVenta}")
    public ResponseEntity<List<ProductoVenta>> findByCategoriaVenta(@PathVariable("categoriaVenta") CategoriasVenta categoriaVenta) {
        List<ProductoVenta> productosVenta = productoVentaService.getProductosVentaByCategoriaVenta(categoriaVenta);
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/precioVenta/{precioVenta}")
    public ResponseEntity<List<ProductoVenta>> findByPrecioVenta(@PathVariable("precioVenta") Double precioVenta) {
        List<ProductoVenta> productosVenta = productoVentaService.getProductosVentaByPrecioVenta(precioVenta);
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/lineaDePedido/{lineaDePedido}")
    public ResponseEntity<List<ProductoVenta>> findByLineaDePedidoId(@PathVariable("lineaDePedido") Integer lineaDePedido) {
        List<ProductoVenta> productosVenta = productoVentaService.getProductosVentaByLineaDePedidoId(lineaDePedido);
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}/precioVenta/{precioVenta}")
    public ResponseEntity<List<ProductoVenta>> findByNombreAndPrecioVenta(@PathVariable("nombre") String nombre, @PathVariable("precioVenta") Double precioVenta) {
        List<ProductoVenta> productosVenta = productoVentaService.getProductosVentaByNombreAndPrecioVenta(nombre, precioVenta);
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @GetMapping("/categoriaVenta/{categoriaVenta}/precioVenta/{precioVenta}")
    public ResponseEntity<List<ProductoVenta>> findByCategoriaVentaAndPrecioVenta(@PathVariable("categoriaVenta") CategoriasVenta categoriaVenta, @PathVariable("precioVenta") Double precioVenta) {
        List<ProductoVenta> productosVenta = productoVentaService.getProductosVentaByCategoriaVentaAndPrecioVenta(categoriaVenta, precioVenta);
        if (productosVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productosVenta, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductoVenta> save(@RequestBody @Valid ProductoVenta productoVenta) {
        if (productoVenta == null) {
            throw new IllegalArgumentException("Producto de venta no puede ser nulo");
        }
        return new ResponseEntity<>(productoVentaService.save(productoVenta), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoVenta> update(@PathVariable("id") String id, @RequestBody @Valid ProductoVenta productoVenta) {
        if (productoVenta == null) {
            throw new IllegalArgumentException("Producto de venta no puede ser nulo");
        }
        if(productoVentaService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productoVenta.setId(Integer.valueOf(id));
        return new ResponseEntity<>(productoVentaService.save(productoVenta), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        ProductoVenta productoVenta = productoVentaService.getById(id);
        if (productoVenta == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productoVentaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
