package ispp_g2.gastrostock.plato;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<Plato> getPlayers(@PathVariable("id") int id) {
		Plato achievementToGet = platoService.getById(id);
		if (achievementToGet == null)
			throw new ResourceNotFoundException("Player with id " + id + " not found!");
		return new ResponseEntity<Plato>(achievementToGet, HttpStatus.OK);
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

}
