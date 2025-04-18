package ispp_g2.gastrostock.mesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

	private final MesaService mesaService;
	private final UserService userService;
	private final NegocioService negocioService;
	private final DuenoService duenoService;
	private final EmpleadoService empleadoService;

	private static final String DUENO = "dueno";
	private static final String EMPLEADO = "empleado";
	private static final String ADMIN = "admin";

	@Autowired
	public MesaController(MesaService mesaService, UserService userService,
		NegocioService negocioService, EmpleadoService empleadoService, DuenoService duenoService) {
		this.mesaService = mesaService;
		this.userService = userService;
		this.negocioService = negocioService;
		this.duenoService = duenoService;
		this.empleadoService = empleadoService;
	}

	@GetMapping
	public ResponseEntity<List<Mesa>> findAll() {
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.getMesas(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Mesa> findById(@PathVariable("id") Integer id) {
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.getById(id), HttpStatus.OK);
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			Mesa mesa = mesaService.getById(id);
			if(mesa.getNegocio().getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(mesaService.getById(id), HttpStatus.OK);
			}
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			Mesa mesa = mesaService.getById(id);
			if(mesa.getNegocio().getId().equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(mesaService.getById(id), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/dto/{id}")
	public ResponseEntity<MesaDTO> findByIdDTO(@PathVariable("id") Integer id) {
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(MesaDTO.of(mesaService.getById(id)), HttpStatus.OK);
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			Mesa mesa = mesaService.getById(id);
			if(mesa.getNegocio().getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(MesaDTO.of(mesaService.getById(id)), HttpStatus.OK);
			}
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			Mesa mesa = mesaService.getById(id);
			if(mesa.getNegocio().getId().equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(MesaDTO.of(mesaService.getById(id)), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/numeroAsientos/{numeroAsientos}")
	public ResponseEntity<List<Mesa>> findByNumeroAsientos(@PathVariable("numeroAsientos") Integer numeroAsientos) {
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.getMesasByNumeroAsientos(numeroAsientos), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/negocio/{negocioId}")
	public ResponseEntity<List<Mesa>> findByNegocio(@PathVariable("negocioId") Integer negocioId) {
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.getMesasByNegocio(negocioId), HttpStatus.OK);
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			Negocio negocio = negocioService.getById(negocioId);
			if(negocio.getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(mesaService.getMesasByNegocio(negocioId), HttpStatus.OK);
			}	
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			if(negocioId.equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(mesaService.getMesasByNegocio(negocioId), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("/dto/negocio/{negocioId}")
	public ResponseEntity<List<MesaDTO>> findByNegocioDto(@PathVariable("negocioId") Integer negocioId) {
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.getMesasByNegocio(negocioId)
					.stream().map(MesaDTO::of).toList(), HttpStatus.OK);
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			Negocio negocio = negocioService.getById(negocioId);
			if(negocio.getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(mesaService.getMesasByNegocio(negocioId)
				.stream().map(MesaDTO::of).toList(), HttpStatus.OK);
			}	
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			if(negocioId.equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(mesaService.getMesasByNegocio(negocioId)
				.stream().map(MesaDTO::of).toList(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PostMapping
	public ResponseEntity<Mesa> create(@RequestBody @Valid Mesa mesa) {
		if(mesa == null) {
			throw new IllegalArgumentException("Mesa cannot be null");
		}
		
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.CREATED);
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			if(mesa.getNegocio().getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.CREATED);
			}
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			if(mesa.getNegocio().getId().equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PostMapping("/dto")
	public ResponseEntity<Mesa> createDto(@RequestBody @Valid MesaDTO mesaDto) {
		if(mesaDto == null) {
			throw new IllegalArgumentException("Mesa cannot be null");
		}
		
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			Mesa mesa = mesaService.convertirMesa(mesaDto);
			return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.CREATED);
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Mesa mesa = mesaService.convertirMesa(mesaDto);
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			if(mesa.getNegocio().getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.CREATED);
			}
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Mesa mesa = mesaService.convertirMesa(mesaDto);
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			if(mesa.getNegocio().getId().equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(mesaService.save(mesa), HttpStatus.CREATED);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Mesa> update(@PathVariable("id") Integer id, @RequestBody @Valid Mesa mesa) {
		if(mesa == null) {
			throw new IllegalArgumentException("Mesa cannot be null");
		}
		if(!mesaService.getById(id).getNegocio().getId().equals(mesa.getNegocio().getId())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Si el negocio de la mesa no coincide con el de la mesa en la bdd, no se puede actualizar la mesa
		}
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.update(mesa, id), HttpStatus.OK);		
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			if(mesa.getNegocio().getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(mesaService.update(mesa, id), HttpStatus.OK);
			}	
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			if(mesa.getNegocio().getId().equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(mesaService.update(mesa, id), HttpStatus.OK);		
			}	
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PutMapping("/dto/{id}")
	public ResponseEntity<Mesa> updateDto(@PathVariable("id") Integer id, @RequestBody @Valid MesaDTO mesaDto) {
		if(mesaDto == null) {
			throw new IllegalArgumentException("Mesa cannot be null");
		}
		Mesa mesa = mesaService.convertirMesa(mesaDto);
		if(!mesaService.getById(id).getNegocio().getId().equals(mesa.getNegocio().getId())) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Si el negocio de la mesa no coincide con el de la mesa en la bdd, no se puede actualizar la mesa
		}
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			return new ResponseEntity<>(mesaService.update(mesa, id), HttpStatus.OK);		
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());
			if(mesa.getNegocio().getDueno().getId().equals(dueno.getId())) {
				return new ResponseEntity<>(mesaService.update(mesa, id), HttpStatus.OK);
			}	
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			if(mesa.getNegocio().getId().equals(empleado.getNegocio().getId())) {
				return new ResponseEntity<>(mesaService.update(mesa, id), HttpStatus.OK);		
			}	
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
		User user = userService.findCurrentUser();
		if(user.hasAnyAuthority(ADMIN).equals(true)) {
			mesaService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else if(user.hasAnyAuthority(DUENO).equals(true)) {
			Dueno dueno = duenoService.getDuenoByUser(user.getId());	
			Mesa mesa = mesaService.getById(id);
			if(mesa.getNegocio().getDueno().getId().equals(dueno.getId())) {
				mesaService.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
			Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
			Mesa mesa = mesaService.getById(id);
			if(mesa.getNegocio().getId().equals(empleado.getNegocio().getId())) {
				mesaService.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
}