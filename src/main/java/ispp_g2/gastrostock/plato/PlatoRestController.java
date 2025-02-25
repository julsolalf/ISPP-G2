package ispp_g2.gastrostock.plato;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/{matchId}/plato")
public class PlatoRestController {

    private final PlatoService platoService;

    @Autowired
	public PlatoRestController(PlatoService platoService) {
		this.platoService = platoService;
	}

	@GetMapping
	public ResponseEntity<List<Plato>> findAll() {
		return new ResponseEntity<>((List<Plato>) platoService.getPlatos(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Plato> getPlato(@PathVariable("id") int id) {
		Plato platoToGet = platoService.getById(id);
		if (platoToGet == null)
			throw new ResourceNotFoundException("Plato with id " + id + " not found!");
		return new ResponseEntity<Plato>(platoToGet, HttpStatus.OK);
	}

	@GetMapping("/{name}")
	public ResponseEntity<Plato> getPlatoByName(@PathVariable("name") String name) {
		Plato platoToGet = platoService.getByName(name);
		if (platoToGet == null)
			throw new ResourceNotFoundException("Plato with name " + name + " not found!");
		return new ResponseEntity<Plato>(platoToGet, HttpStatus.OK);
	}

	@GetMapping("/{categoria}")
	public ResponseEntity<List<Plato>> getPlatosByCategoria(@PathVariable("categoria") String categoria) {
		List<Plato> platoToGet = platoService.getPlatoByCategoria(categoria);
		if (platoToGet == null)
			throw new ResourceNotFoundException("Platos with that categoria " + categoria + " not found!");
		return new ResponseEntity<List<Plato>>(platoToGet, HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Plato> createPlato(@RequestBody @Valid Plato newPlato,
			BindingResult br) {
		Plato result = null;
		if (!br.hasErrors())
			result = platoService.savePlato(newPlato);
		else
			throw new ispp_g2.gastrostock.exceptions.BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlato(@PathVariable("id") int id) {
		Plato platoToGet = platoService.getById(id);
		if (platoToGet == null)
			throw new ResourceNotFoundException("Plato with id " + id + " not found!");
		platoService.deletePlato(platoToGet);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("update/{id}")
	public ResponseEntity<Void> updatePlato(@PathVariable("id") Integer id, String name, String descripcion, Cantidad cantidad, String categoria, Double precio) {
		Plato platoToUpdate = platoService.getById(id);

		platoToUpdate.setName(name);
		platoToUpdate.setDescripcion(descripcion);
		platoToUpdate.setCantidad(cantidad);
		platoToUpdate.setCategoria(categoria);
		platoToUpdate.setPrecio(precio);
		platoService.savePlato(platoToUpdate);

		return ResponseEntity.noContent().build();
	}

}
