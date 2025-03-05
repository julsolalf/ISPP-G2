package ispp_g2.gastrostock.Usuario;

import java.util.List;
import java.util.Random;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.AuthenticationFailedException;
import ispp_g2.gastrostock.exceptions.ResourceAlreadyExistsException;
import jakarta.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;


@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;

    @Autowired
    public UsuarioService(UsuarioRepository repo, JavaMailSender emailSender) {
        this.repo = repo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailSender = emailSender;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario getByName(String nombre) {
        return repo.findByUsername(nombre);
    }

    @Transactional
    public Usuario saveUsuario(@Valid Usuario newUsuario) {
        return repo.save(newUsuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarioByTipo(TipoUsuario tipo) {
        return repo.findByTipo(tipo);
    }

    @Transactional
    public void deleteUsuario(Usuario u) {
        repo.delete(u);
    }

    public Usuario getById(Integer id) {
        return repo.getById();
    }

    @Transactional
    public Usuario registerUsuario(@Valid Usuario newUsuario) {
        // Validar si el username o el email ya existen
        if (repo.findByUsername(newUsuario.getUsername()) != null) {
            throw new ResourceAlreadyExistsException("El nombre de usuario ya está en uso.");
        }
        if (repo.findByEmail(newUsuario.getEmail()) != null) {
            throw new ResourceAlreadyExistsException("El email ya está en uso.");
        }

        // Guardar el nuevo usuario
        return repo.save(newUsuario);
    }

    @Transactional
    public Usuario login(String username, String password) {
        Usuario usuario = repo.findByUsername(username);
        if (usuario == null || !passwordEncoder.matches(password, usuario.getPassword())) {
            throw new AuthenticationFailedException("Nombre de usuario o contraseña incorrectos.");
        }
        return usuario;
    }

    @Transactional
    @Async
    public void enviarCodigoRecuperacion(String email) {
        Usuario usuario = repo.findByEmail(email);
        if (usuario == null) {
            throw new ResourceNotFoundException("No se encontró ningún usuario con ese correo electrónico.");
        }
        
        String codigo = generarCodigoRecuperacion();
        enviarEmailRecuperacion(usuario.getEmail(), codigo);
        
        // Guardar el código en la base de datos para verificarlo después (implementa la lógica según tu diseño)
        usuario.setCodigoRecuperacion(codigo);
        repo.save(usuario);
    }

    private String generarCodigoRecuperacion() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // Código de 6 dígitos
    }

    private void enviarEmailRecuperacion(String email, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de recuperación de contraseña");
        message.setText("Tu código de recuperación es: " + codigo);
        emailSender.send(message);
    }

    @Transactional
    public void restablecerPassword(String email, String codigo, String nuevaContrasena) {
        Usuario usuario = repo.findByEmail(email);
        if (usuario == null || !usuario.getCodigoRecuperacion().equals(codigo)) {
            throw new AuthenticationFailedException("El código de recuperación es inválido.");
        }
        
        // Actualizar la contraseña (asegúrate de encriptarla)
        usuario.setPassword(new BCryptPasswordEncoder().encode(nuevaContrasena));
        usuario.setCodigoRecuperacion(null); // Limpiar el código de recuperación
        repo.save(usuario);
    }
    
}
