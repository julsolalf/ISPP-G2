package ispp_g2.gastrostock.empleado;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private static final String SECRET_KEY = "mySecretKeymySecretKeymySecretKeymySecretKeymySecretKey";

    @Autowired
    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
    }

    // Crear o actualizar un empleado
    public Empleado saveEmpleado(Empleado empleado) {
        return repo.save(empleado);
    }

    // Obtener todos los empleados
    public List<Empleado> getAllEmpleados() {
        return (List<Empleado>) repo.findAll();
    }

    // Buscar empleado por ID
    public Optional<Empleado> getEmpleadoById(Integer id) {
        return repo.findById(id);
    }

    // Eliminar empleado
    public void deleteEmpleado(Integer id) {
        repo.deleteById(id);
    }

    // Generar token JWT
    public String generateToken(String tokenEmpleado) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
                .setSubject(tokenEmpleado) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Expira en 1 día
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    // Autenticación de empleado con JWT
    public String authenticateEmpleado(String tokenEmpleado) {
        Optional<Empleado> empleado = repo.findByTokenEmpleado(tokenEmpleado);
        return empleado.isPresent() ? generateToken(tokenEmpleado) : null;
    }

    // Validar token JWT
    public boolean validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
}
