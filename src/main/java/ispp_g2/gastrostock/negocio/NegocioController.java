package ispp_g2.gastrostock.negocio;

import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.exceptions.BadRequestException;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {

    private NegocioService ns;

    @Autowired
    public NegocioController(NegocioService negocioService) {
        this.ns = negocioService;
    }

    @GetMapping
	public ResponseEntity<List<Negocio>> findAll() {
		return new ResponseEntity<>((List<Negocio>) ns.getNegocios(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Negocio> findNegocio(@PathVariable("id") int id) {
		Negocio negocioToGet = ns.getById(id);
		if (negocioToGet == null)
			throw new ResourceNotFoundException("negocio with id " + id + " not found!");
		return new ResponseEntity<Negocio>(negocioToGet, HttpStatus.OK);
	}

    @GetMapping("/token/{token}")
	public ResponseEntity<Negocio> findNegocioByToken(@PathVariable("token") Integer token) {
		Negocio negocioToGet = ns.getByToken(token);
		if (negocioToGet == null)
			throw new ResourceNotFoundException("negocio with token " + token + " not found!");
		return new ResponseEntity<Negocio>(negocioToGet, HttpStatus.OK);
	}

    @GetMapping("/name/{name}")
	public ResponseEntity<Negocio> findNegocioByName(@PathVariable("token") String name) {
		Negocio negocioToGet = ns.getByName(name);
		if (negocioToGet == null)
			throw new ResourceNotFoundException("negocio with name " + name + " not found!");
		return new ResponseEntity<Negocio>(negocioToGet, HttpStatus.OK);
	}

    @GetMapping("/ciudad/{ciudad}")
	public ResponseEntity<List<Negocio>> findNegocioByCiudad(@PathVariable("ciudad") String ciudad) {
		List<Negocio> negociosToGet = ns.getByCiudad(ciudad);
		if (negociosToGet == null)
			throw new ResourceNotFoundException("negocio with ciudad " + ciudad + " not found!");
		return new ResponseEntity<List<Negocio>>(negociosToGet, HttpStatus.OK);
	}

    @GetMapping("/codigoPostal/{codigoPostal}")
	public ResponseEntity<List<Negocio>> findNegocioByCodigoPostal(@PathVariable("codigoPostal") String codigoPostal) {
		List<Negocio> negociosToGet = ns.getByCodigoPostal(codigoPostal);
		if (negociosToGet == null)
			throw new ResourceNotFoundException("negocio with codigoPostal " + codigoPostal + " not found!");
		return new ResponseEntity<List<Negocio>>(negociosToGet, HttpStatus.OK);
	}

    @GetMapping("/pais/{pais}")
	public ResponseEntity<List<Negocio>> findNegocioByPais(@PathVariable("pais") String pais) {
		List<Negocio> negociosToGet = ns.getByPais(pais);
		if (negociosToGet == null)
			throw new ResourceNotFoundException("negocio with pais " + pais + " not found!");
		return new ResponseEntity<List<Negocio>>(negociosToGet, HttpStatus.OK);
	}

    @GetMapping("/direccion/{direccion}")
	public ResponseEntity<Negocio> findNegocioByDireccion(@PathVariable("direccion") String direccion) {
		Negocio negocioToGet = ns.getByDireccion(direccion);
		if (negocioToGet == null)
			throw new ResourceNotFoundException("negocio with direccion " + direccion + " not found!");
		return new ResponseEntity<Negocio>(negocioToGet, HttpStatus.OK);
	}



	@PostMapping
	public ResponseEntity<Negocio> createNegocio(@RequestBody @Valid Negocio newNegocio, BindingResult br) {
		Negocio result = null;
		if (!br.hasErrors())
			result = ns.saveNegocio(newNegocio);
		else
			throw new BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modifyNegocio(@RequestBody @Valid Negocio newNegocio, BindingResult br,
			@PathVariable("id") int id) {
		Negocio negocioToUpdate = this.findNegocio(id).getBody();
		if (br.hasErrors())
			throw new BadRequestException(br.getAllErrors());
		else if (newNegocio.getId() == null || !newNegocio.getId().equals(id))
			throw new BadRequestException("Negocio id is not consistent with resource URL:" + id);
		else {
			BeanUtils.copyProperties(newNegocio, negocioToUpdate, "id");
			ns.saveNegocio(negocioToUpdate);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{token}")
	public ResponseEntity<Void> deletenegocio(@PathVariable("token") Integer token) {
		findNegocioByToken(token);
		ns.deleteNegocioByToken(token);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
