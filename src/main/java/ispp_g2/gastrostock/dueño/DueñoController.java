package ispp_g2.gastrostock.dueño;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/{matchId}/dueño")
public class DueñoController {

    private final DueñoService dueñoService;

    @Autowired
	public DueñoController(DueñoService dueñoService) {
		this.dueñoService = dueñoService;
	}

    // Obtener todos los dueños
    @GetMapping
    public List<Dueño> getAllDueños() {
        return dueñoService.getAllDueños();
    }

    // Obtener dueño por ID
    @GetMapping("/{id}")
    public ResponseEntity<Dueño> getDueñoById(@PathVariable Integer id) {
        return dueñoService.getDueñoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener dueño por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Dueño> getDueñoByEmail(@PathVariable String email) {
        return dueñoService.getDueñoByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo dueño
    @PostMapping
    public Dueño createDueño(@RequestBody Dueño dueño) {
        return dueñoService.saveDueño(dueño);
    }

    // Eliminar un dueño
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDueño(@PathVariable Integer id) {
        dueñoService.deleteDueño(id);
        return ResponseEntity.noContent().build();
    }

    // // Autenticación de dueño
    // @PostMapping("/login")
    // public ResponseEntity<String> authenticateDueño(@RequestParam String email, @RequestParam String password) {
    //     boolean isAuthenticated = dueñoService.authenticateDueño(email, password);
    //     return isAuthenticated ? ResponseEntity.ok("Autenticación exitosa") : ResponseEntity.status(401).body("Credenciales incorrectas");
    // }

    // Autenticación de dueño con JWT
    @PostMapping("/login")
    public ResponseEntity<String> authenticateDueño(@RequestParam String email, @RequestParam String tokenDueño) {
        String jwt = dueñoService.authenticateDueño(email, tokenDueño);
        return (jwt != null) ? ResponseEntity.ok(jwt) : ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    // Validar token JWT
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        boolean isValid = dueñoService.validateToken(token);
        return isValid ? ResponseEntity.ok("Token válido") : ResponseEntity.status(401).body("Token inválido o expirado");
    }
    
}
