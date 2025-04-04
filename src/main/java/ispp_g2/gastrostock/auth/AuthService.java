package ispp_g2.gastrostock.auth;

import java.util.Random;
import java.util.regex.Pattern;

import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ispp_g2.gastrostock.auth.payload.request.LoginRequest;
import ispp_g2.gastrostock.auth.payload.request.RegisterRequest;
import ispp_g2.gastrostock.auth.payload.response.AuthResponse;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.user.AuthoritiesService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import ispp_g2.gastrostock.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final DuenoService duenoService;
    private final PasswordEncoder encoder;
    private final AuthoritiesService authoritiesService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findUserByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.getToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtToken);
        return authResponse;
    }

    @Transactional
    public AuthResponse register(@Valid RegisterRequest request) throws BadRequestException {
        if(!validarPassword(request.getPassword())) {
            throw new BadRequestException("La contraseña debe tener entre 8 y 32 caracteres, 1 mayúscula, " +
            "1 minúscula, un número y un caracter especial");
        }
        if(!validarTelefono(request.getNumTelefono())) {
            throw new BadRequestException("El teléfono debe ser correcto");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setAuthority(authoritiesService.findByAuthority("dueno"));
        userService.saveUser(user);

        Dueno owner = new Dueno();
        owner.setFirstName(request.getFirstName());
        owner.setLastName(request.getLastName());
        owner.setEmail(request.getEmail());
        owner.setNumTelefono(request.getNumTelefono());
        owner.setTokenDueno(generarToken()+user.getId());
        owner.setUser(user);
        duenoService.saveDueno(owner);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.getToken(user));
        return authResponse;

    }

    private boolean validarPassword(String password) {
        Pattern pattern = Pattern.compile(
            "^(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            "(?=.*\\d)" +
            "(?=.*[#$@!%&?¡\"+,.:;='^|~_()¿{}\\[\\]\\\\-])" +
            ".{8,32}$"
        );
        return pattern.matcher(password).matches();
    }

    private boolean validarTelefono(String telefono) {
        Pattern pattern = Pattern.compile("^[6789]\\d{8}$");
        return pattern.matcher(telefono).matches();
    }


    private String generarToken() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Integer l = 30;
        Random r = new Random();

        StringBuilder sb = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            int index = r.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return "gst-" + sb.toString();
    }

}
