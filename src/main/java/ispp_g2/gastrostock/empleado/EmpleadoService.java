package ispp_g2.gastrostock.empleado;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private static final String SECRET_KEY = "mySecretKey";

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
        return Jwts.builder()
                .setSubject(tokenEmpleado)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Autenticación de empleado con JWT
    public String authenticateEmpleado(String tokenEmpleado) {
        Optional<Empleado> empleado = repo.findByTokenEmpleado(tokenEmpleado);
        return empleado.isPresent() ? generateToken(tokenEmpleado) : null;
    }

    // Validar token JWT
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
}
