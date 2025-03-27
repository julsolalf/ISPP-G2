package ispp_g2.gastrostock.mesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

	private final MesaService mesaService;

	@Autowired
	public MesaController(MesaService mesaService) {
		this.mesaService = mesaService;
	}

	@GetMapping
	public ResponseEntity<List<Mesa>> findAll() {
		if (mesaService.getMesas().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(mesaService.getMesas(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Mesa> findById(@PathVariable("id") String id) {
		Mesa mesa = mesaService.getById(id);
		if (mesa == null) {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(mesa, HttpStatus.OK);
	}

	@GetMapping("/numeroAsientos/{numeroAsientos}")
	public ResponseEntity<List<Mesa>> findByNumeroAsientos(@PathVariable("numeroAsientos") Integer numeroAsientos) {
		List<Mesa> mesas = mesaService.getMesasByNumeroAsientos(numeroAsientos);
		if (mesas == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(mesas, HttpStatus.OK);
	}

	@GetMapping("/negocio/{negocioId}")
	public ResponseEntity<List<Mesa>> findByNegocio(@PathVariable("negocioId") String negocioId) {
		List<Mesa> mesas = mesaService.getMesasByNegocio(negocioId);
		if (mesas == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(mesas, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Mesa> create(@RequestBody @Valid Mesa mesa) {
		if(mesa == null) {
			throw new IllegalArgumentException("Mesa cannot be null");
		}
		return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Mesa> update(@PathVariable("id") String id, @RequestBody @Valid Mesa mesa) {
		if(mesa == null) {
			throw new IllegalArgumentException("Mesa cannot be null");
		}
		Mesa existingMesa = mesaService.getById(id);
		if (existingMesa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		mesa.setId(Integer.valueOf(id));
		return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		Mesa mesa = mesaService.getById(id);
		if (mesa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		mesaService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}