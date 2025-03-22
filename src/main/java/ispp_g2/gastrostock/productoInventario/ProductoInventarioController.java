package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ispp_g2.gastrostock.exceptions.BadRequestException;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@RestController
public class ProductoInventarioController {

    private ProductoInventarioService ps;

    @GetMapping
	public ResponseEntity<List<ProductoInventario>> findAll() {
		return new ResponseEntity<>((List<ProductoInventario>) ps.getProductosInventario(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductoInventario> findProductoInventario(@PathVariable("id") int id) {
		ProductoInventario ProductoInventarioToGet = ps.getById(id);
		if (ProductoInventarioToGet == null)
			throw new ResourceNotFoundException("ProductoInventario with id " + id + " not found!");
		return new ResponseEntity<ProductoInventario>(ProductoInventarioToGet, HttpStatus.OK);
	}



    @GetMapping("/categoria/{categoria}")
	public ResponseEntity<List<ProductoInventario>> findByNumeroAsientos(@PathVariable("categoria") CategoriasInventario categoriasInventario) {
		List<ProductoInventario> ProductoInventariosToGet = ps.getProductoInventarioByCategoria(categoriasInventario);
		if (ProductoInventariosToGet == null)
			throw new ResourceNotFoundException("ProductoInventario with this categoria " + categoriasInventario + " not found!");
		return new ResponseEntity<List<ProductoInventario>>(ProductoInventariosToGet, HttpStatus.OK);
	}

    @GetMapping("/name/{name}")
	public ResponseEntity<ProductoInventario> findByName(@PathVariable("name") String name) {
		ProductoInventario ProductoInventarioToGet = ps.getByName(name);
		if (ProductoInventarioToGet == null)
			throw new ResourceNotFoundException("ProductoInventario with name " + name + " not found!");
		return new ResponseEntity<ProductoInventario>(ProductoInventarioToGet, HttpStatus.OK);
	}

    @PutMapping("/{id}")
	public ResponseEntity<Void> modifyProductoInventario(@RequestBody @Valid ProductoInventario newProductoInventario, BindingResult br,
			@PathVariable("id") int id) {
		ProductoInventario ProductoInventarioToUpdate = this.findProductoInventario(id).getBody();
		if (br.hasErrors())
			throw new BadRequestException(br.getAllErrors());
		else if (newProductoInventario.getId() == null || !newProductoInventario.getId().equals(id))
			throw new BadRequestException("ProductoInventario id is not consistent with resource URL:" + id);
		else {
			BeanUtils.copyProperties(newProductoInventario, ProductoInventarioToUpdate, "id");
			ps.saveProductoInventario(ProductoInventarioToUpdate);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductoInventario(@PathVariable("id") int id) {
        ps.deleteProductoInventario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
