package ispp_g2.gastrostock.Usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/{matchId}/usuario")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    @Autowired
	public UsuarioRestController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

    @GetMapping
	public ResponseEntity<List<Usuario>> findAll() {
		return new ResponseEntity<>((List<Usuario>) usuarioService.getUsuarios(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUsuarios(@PathVariable("id") int id) {
		Usuario achievementToGet = usuarioService.getById(id);
		if (achievementToGet == null)
			throw new ResourceNotFoundException("User with id " + id + " not found!");
		return new ResponseEntity<Usuario>(achievementToGet, HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Usuario> createUsuario(@RequestBody @Valid Usuario newUsuario,
			BindingResult br) {
		Usuario result = null;
		if (!br.hasErrors())
			result = usuarioService.saveUsuario(newUsuario);
		else
			throw new ispp_g2.gastrostock.exceptions.BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Usuario> registerUsuario(@RequestBody @Valid Usuario newUsuario, BindingResult br) {
        if (br.hasErrors()) {
            throw new ispp_g2.gastrostock.exceptions.BadRequestException(br.getAllErrors());
        }
        Usuario result = usuarioService.registerUsuario(newUsuario);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(@RequestBody String email) {
        usuarioService.enviarCodigoRecuperacion(email);
        return new ResponseEntity<>("Se ha enviado un código de recuperación a tu correo electrónico.", HttpStatus.OK);
    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<String> restablecerPassword(@RequestBody RestablecerPasswordRequest request) {
        usuarioService.restablecerPassword(request.getEmail(), request.getCodigo(), request.getNuevaContrasena());
        return new ResponseEntity<>("La contraseña ha sido restablecida con éxito.", HttpStatus.OK);
    }


    @PostMapping("/{negocioId}/empleado/{usuarioId}")
    public ResponseEntity<Usuario> agregarEmpleado(@PathVariable Integer negocioId, @PathVariable Integer usuarioId) {
        Usuario usuario = usuarioService.agregarEmpleado(negocioId, usuarioId);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping("/{negocioId}/empleado/{usuarioId}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer negocioId, @PathVariable Integer usuarioId) {
        usuarioService.eliminarEmpleado(negocioId, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
