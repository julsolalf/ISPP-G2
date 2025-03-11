package ispp_g2.gastrostock.dueño;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@Service
public class DueñoService {

    private final DueñoRepository repo;
    private static final String SECRET_KEY = "mySecretKey";

    @Autowired
    public DueñoService(DueñoRepository repo) {
        this.repo = repo;

    }

     // Crear o actualizar un dueño
     public Dueño saveDueño(Dueño dueño) {
        return repo.save(dueño);
    }

    // Obtener todos los dueños
    public List<Dueño> getAllDueños() {
        return repo.findAll();
    }

    // Buscar dueño por ID
    public Optional<Dueño> getDueñoById(Integer id) {
        return repo.findById(id);
    }

    // Buscar dueño por email
    public Optional<Dueño> getDueñoByEmail(String email) {
        return repo.findByEmail(email);
    }

    // Eliminar dueño
    public void deleteDueño(Integer id) {
        repo.deleteById(id);
    }

    // // Verificar autenticación básica (sin encriptación)
    // public boolean authenticateDueño(String email, String contraseña) {
    //     Optional<Dueño> dueño = repo.findByEmail(email);
    //     return dueño.isPresent() && dueño.get().getContraseña().equals(contraseña);
    // }

    // Generar token JWT
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Expira en 1 día
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Verificar autenticación y generar token JWT
    public String authenticateDueño(String email, String tokenDueño) {
        Optional<Dueño> dueño = repo.findByEmail(email);
        if (dueño.isPresent() && dueño.get().getTokenDueño().equals(tokenDueño)) {
            return generateToken(email);
        }
        return null;
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
