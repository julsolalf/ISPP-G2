package ispp_g2.gastrostock.auth;

import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ispp_g2.gastrostock.auth.payload.request.LoginRequest;
import ispp_g2.gastrostock.auth.payload.request.RegisterRequest;
import ispp_g2.gastrostock.auth.payload.response.AuthResponse;
import ispp_g2.gastrostock.config.jwt.JwtService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoRepository;
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
    public AuthResponse register(@Valid RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        System.out.println(user.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setAuthority(authoritiesService.findByAuthority("dueño"));
        userService.saveUser(user);

        Dueno owner = new Dueno();
        owner.setFirstName(request.getFirstName());
        System.out.println(owner.getFirstName());
        owner.setLastName(request.getLastName());
        System.out.println(owner.getLastName());
        owner.setEmail(request.getEmail());
        System.out.println(owner.getEmail());
        owner.setNumTelefono(request.getNumTelefono());
        System.out.println(owner.getNumTelefono());
        owner.setTokenDueno(generarToken()+user.getId());
        System.out.println(owner.getTokenDueno());
        owner.setUser(user);
        System.out.println(owner.getUser().getId());
        duenoService.saveDueno(owner);
        System.out.println(owner.getId());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.getToken(user));
        return authResponse;

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
