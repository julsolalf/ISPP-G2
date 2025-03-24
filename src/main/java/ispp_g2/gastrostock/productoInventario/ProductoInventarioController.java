package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.exceptions.BadRequestException;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productosInventario")
public class ProductoInventarioController {

    private final ProductoInventarioService productoInventarioService;

	@Autowired
	public ProductoInventarioController(ProductoInventarioService productoInventarioService) {
		this.productoInventarioService = productoInventarioService;
	}

    @GetMapping
	public ResponseEntity<List<ProductoInventario>> findAll() {
		if(productoInventarioService.getProductosInventario().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(productoInventarioService.getProductosInventario(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductoInventario> findProductoInventario(@PathVariable("id") String id) {
		ProductoInventario productoInventario = productoInventarioService.getById(id);
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

    @GetMapping("/categoria/{categoria}")
	public ResponseEntity<List<ProductoInventario>> findByNumeroAsientos(@PathVariable("categoria") CategoriasInventario categoriasInventario) {
		List<ProductoInventario> productoInventario = productoInventarioService.getProductoInventarioByCategoria(categoriasInventario);
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

    @GetMapping("/name/{name}")
	public ResponseEntity<ProductoInventario> findByName(@PathVariable("name") String name) {
		ProductoInventario productoInventario = productoInventarioService.getByName(name);
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/precioCompra/{precioCompra}")
	public ResponseEntity<List<ProductoInventario>> findByPrecioCompra(@PathVariable("precioCompra") Double precioCompra) {
		List<ProductoInventario> productoInventario = productoInventarioService.getProductoInventarioByPrecioCompra(precioCompra);
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/cantidadDeseada/{cantidadDeseada}")
	public ResponseEntity<List<ProductoInventario>> findByCantidadDeseada(@PathVariable("cantidadDeseada") Integer cantidadDeseada) {
		List<ProductoInventario> productoInventario = productoInventarioService.getProductoInventarioByCantidadDeseada(cantidadDeseada);
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/cantidadAviso/{cantidadAviso}")
	public ResponseEntity<List<ProductoInventario>> findByCantidadAviso(@PathVariable("cantidadAviso") Integer cantidadAviso) {
		List<ProductoInventario> productoInventario = productoInventarioService.getProductoInventarioByCantidadAviso(cantidadAviso);
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<ProductoInventario> createProductoInventario(@RequestBody @Valid ProductoInventario newProductoInventario) {
		if (newProductoInventario==null)
			throw new IllegalArgumentException("ProductoInventario cannot be null");
		return new ResponseEntity<>(productoInventarioService.save(newProductoInventario), HttpStatus.CREATED);
	}

    @PutMapping("/{id}")
	public ResponseEntity<ProductoInventario> update(@RequestBody @Valid ProductoInventario newProductoInventario, @PathVariable("id") String id) {
		if (newProductoInventario == null)
			throw new BadRequestException("ProductoInventario cannot be null");
		ProductoInventario productoInventario = productoInventarioService.getById(id);
		if (productoInventario == null)
			throw new ResourceNotFoundException("ProductoInventario not found");
		newProductoInventario.setId(Integer.valueOf(id));
		return new ResponseEntity<>(productoInventarioService.save(newProductoInventario), HttpStatus.OK);
	}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        ProductoInventario productoInventario = productoInventarioService.getById(id);
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		productoInventarioService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
