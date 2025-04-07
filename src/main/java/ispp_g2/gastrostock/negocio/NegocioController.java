package ispp_g2.gastrostock.negocio;

import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {

	private final NegocioService negocioService;
	private final UserService userService;
	private final DuenoService duenoService;

	private final String admin = "admin";

	@Autowired
	public NegocioController(NegocioService negocioService, UserService userService, DuenoService duenoService) {
		this.negocioService = negocioService;
		this.userService = userService;
		this.duenoService = duenoService;
	}

	@GetMapping
	public ResponseEntity<List<Negocio>> findAll() {
		User user = userService.findCurrentUser();
		List<Negocio> negocios = negocioService.getByDueno(duenoService.getDuenoByUser(user.getId()).getId());
		if (negocios.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(negocios, HttpStatus.OK);
	}

	@GetMapping("/dto")
	public ResponseEntity<List<NegocioDTO>> findAllDTO() {
		User user = userService.findCurrentUser();
		List<Negocio> negocios = negocioService.getByDueno(duenoService.getDuenoByUser(user.getId()).getId());
		if (negocios.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		List<NegocioDTO> negociosDto = negocios
				.stream()
				.map(negocioService::convertirNegocioDTO)
				.toList();
		return new ResponseEntity<>(negociosDto, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Negocio> findNegocio(@PathVariable("id") Integer id) {
		User user = userService.findCurrentUser();
		Negocio negocioToGet = negocioService.getById(id);
		if (negocioToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (negocioToGet.getDueno().getUser() != user)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
	}

	@GetMapping("/dto/{id}")
	public ResponseEntity<NegocioDTO> findNegocioDTO(@PathVariable("id") Integer id) {
		User user = userService.findCurrentUser();
		Negocio negocioToGet = negocioService.getById(id);
		if (negocioToGet == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (negocioToGet.getDueno().getUser() != user)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(negocioService.convertirNegocioDTO(negocioToGet), HttpStatus.OK);
	}

	@GetMapping("/token/{token}")
	public ResponseEntity<Negocio> findNegocioByToken(@PathVariable("token") Integer token) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority(admin).equals(true)) {
			Negocio negocioToGet = negocioService.getByToken(token);
			if (negocioToGet == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<Negocio> findNegocioByName(@PathVariable("name") String name) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority(admin).equals(true)) {
			Negocio negocioToGet = negocioService.getByName(name);
			if (negocioToGet == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/ciudad/{ciudad}")
	public ResponseEntity<List<Negocio>> findNegocioByCiudad(@PathVariable("ciudad") String ciudad) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority(admin).equals(true)) {
			List<Negocio> negocioToGet = negocioService.getByCiudad(ciudad);
			if (negocioToGet.isEmpty())
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/codigoPostal/{codigoPostal}")
	public ResponseEntity<List<Negocio>> findNegocioByCodigoPostal(@PathVariable("codigoPostal") String codigoPostal) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority(admin).equals(true)) {
			List<Negocio> negocioToGet = negocioService.getByCodigoPostal(codigoPostal);
			if (negocioToGet.isEmpty())
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/pais/{pais}")
	public ResponseEntity<List<Negocio>> findNegocioByPais(@PathVariable("pais") String pais) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority(admin).equals(true)) {
			List<Negocio> negocioToGet = negocioService.getByPais(pais);
			if (negocioToGet.isEmpty())
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/direccion/{direccion}")
	public ResponseEntity<List<Negocio>> findNegocioByDireccion(@PathVariable("direccion") String direccion) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority("admin").equals(true)) {
			List<Negocio> negocioToGet = negocioService.getByDireccion(direccion);
			if (negocioToGet.isEmpty())
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/dueno/{dueno}")
	public ResponseEntity<List<Negocio>> findNegocioByDueno(@PathVariable("dueno") Integer dueno) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority().equals(true)) {
			List<Negocio> negocioToGet = negocioService.getByDueno(dueno);
			if (negocioToGet.isEmpty())
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(negocioToGet, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping
	public ResponseEntity<Negocio> save(@RequestBody @Valid NegocioDTO newNegocio) {
		User user = userService.findCurrentUser();
		if (newNegocio == null)
			throw new IllegalArgumentException("Negocio cannot be null");

		Negocio negocio = negocioService.convertirDTONegocio(newNegocio);
		negocio.setTokenNegocio(generarToken());
		negocio.setDueno(duenoService.getDuenoByUser(user.getId()));
		return new ResponseEntity<>(negocioService.save(negocio), HttpStatus.CREATED);
	}

	private Integer generarToken() {
		Random r = new Random();
		return r.nextInt(000000000, 999999999);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Negocio> update(@RequestBody @Valid NegocioDTO newNegocio,
			@PathVariable("id") Integer id) {

		Negocio toUpdate = negocioService.getById(id);
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority("dueno").equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			if (newNegocio == null)
				throw new IllegalArgumentException("Negocio cannot be null");
			if (!toUpdate.getDueno().getId().equals(dueno.getId()))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			Negocio nuevo = negocioService.convertirDTONegocio(newNegocio);
			return new ResponseEntity<>(negocioService.update(id, nuevo), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
		User user = userService.findCurrentUser();
		if (user.hasAnyAuthority("dueno").equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			Negocio toDelete = negocioService.getById(id);
			if (toDelete == null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			if (!toDelete.getDueno().getId().equals(dueno.getId()))
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			negocioService.delete(id);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

}
