package ispp_g2.gastrostock.negocio;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {

    private final NegocioService negocioService;

    @Autowired
    public NegocioController(NegocioService negocioService) {
        this.negocioService = negocioService;
    }

    @GetMapping
	public ResponseEntity<List<Negocio>> findAll() {
		if(negocioService.getNegocios().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(negocioService.getNegocios(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Negocio> findNegocio(@PathVariable("id") Integer id) {
		Negocio negocioToGet = negocioService.getById(id);
		if (negocioToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
	}

    @GetMapping("/token/{token}")
	public ResponseEntity<Negocio> findNegocioByToken(@PathVariable("token") Integer token) {
		Negocio negocioToGet = negocioService.getByToken(token);
		if (negocioToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
	}

    @GetMapping("/name/{name}")
	public ResponseEntity<Negocio> findNegocioByName(@PathVariable("name") String name) {
		Negocio negocioToGet = negocioService.getByName(name);
		if (negocioToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
	}

    @GetMapping("/ciudad/{ciudad}")
	public ResponseEntity<List<Negocio>> findNegocioByCiudad(@PathVariable("ciudad") String ciudad) {
		List<Negocio> negociosToGet = negocioService.getByCiudad(ciudad);
		if (negociosToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negociosToGet, HttpStatus.OK);
	}

    @GetMapping("/codigoPostal/{codigoPostal}")
	public ResponseEntity<List<Negocio>> findNegocioByCodigoPostal(@PathVariable("codigoPostal") String codigoPostal) {
		List<Negocio> negociosToGet = negocioService.getByCodigoPostal(codigoPostal);
		if (negociosToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negociosToGet, HttpStatus.OK);
	}

    @GetMapping("/pais/{pais}")
	public ResponseEntity<List<Negocio>> findNegocioByPais(@PathVariable("pais") String pais) {
		List<Negocio> negociosToGet = negocioService.getByPais(pais);
		if (negociosToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negociosToGet, HttpStatus.OK);
	}

    @GetMapping("/direccion/{direccion}")
	public ResponseEntity<List<Negocio>> findNegocioByDireccion(@PathVariable("direccion") String direccion) {
		List<Negocio> negocioToGet = negocioService.getByDireccion(direccion);
		if (negocioToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
	}

	@GetMapping("/dueño/{dueño}")
	public ResponseEntity<List<Negocio>> findNegocioByDueño(@PathVariable("dueño") Integer dueño) {
		List<Negocio> negocioToGet = negocioService.getByDueño(dueño);
		if (negocioToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Negocio> save(@RequestBody @Valid Negocio newNegocio) {
		if (newNegocio == null)
			throw new IllegalArgumentException("Negocio cannot be null");
		return new ResponseEntity<>(negocioService.save(newNegocio), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Negocio> update(@RequestBody @Valid Negocio newNegocio,
			@PathVariable("id") Integer id) {
		if(newNegocio == null)
			throw new IllegalArgumentException("Negocio cannot be null");
		if(negocioService.getById(id) == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		newNegocio.setId(id);
		return new ResponseEntity<>(negocioService.save(newNegocio), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
		Negocio negocio = negocioService.getById(id);
		if (negocio == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		negocioService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
