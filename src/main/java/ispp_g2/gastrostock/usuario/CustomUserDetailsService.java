package ispp_g2.gastrostock.usuario;

import ispp_g2.gastrostock.usuario.Usuario;
import ispp_g2.gastrostock.usuario.UsuarioService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.getByName(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username);
        }

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())  // Spring Security manejará la encriptación
                .roles(usuario.getTipo().name())  // Usa el TipoUsuario como rol
                .build();
    }
}

