package ispp_g2.gastrostock.config.jwt;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {

    private final DuenoService duenoService;
    private final EmpleadoService empleadoService;
    private final UserService userService;

    private static final String SECRET_KEY = "GastroStockSecretKeyGastroStockSecretKeyGastroStockSecretKeyGastroStockSecretKeyGastroStockSecretKeyGastroStockSecretKey";

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(),user);
            }
        
    private String getToken(Map<String, Object> extraClaim, UserDetails user) {
        return Jwts.builder()
            .setClaims(extraClaim)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
            .signWith(getKey(user),SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getKey(UserDetails userDetails) {
        /*User user = userService.findUserByUsername(userDetails.getUsername());
        Authorities authority = user.getAuthority();
        switch (authority.getAuthority()) {
            case "dueno":
                Dueno dueno = duenoService.getDuenoByUser(user.getId());
                byte[] keyBytesDueno = Decoders.BASE64.decode(
                    dueno.getTokenDueno().substring(4)+ 
                    SECRET_KEY);
                return Keys.hmacShaKeyFor(keyBytesDueno);
            default:
                Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
                byte[] keyBytesEmpleado = Decoders.BASE64.decode(
                    empleado.getTokenEmpleado().substring(4)+ 
                    SECRET_KEY);
                return Keys.hmacShaKeyFor(keyBytesEmpleado);
        }*/
        byte[] keyBytesEmpleado = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytesEmpleado);
    }

    public boolean validateJwtToken(String jwt, UserDetails userDetails) {
        String username = getUserNameFromJwtToken(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

	public String getUserNameFromJwtToken(String token) {
		return getClaim(token, Claims::getSubject);
	}

    private Claims getAllClaims(String jwt) {

        return Jwts.parserBuilder()
            .setSigningKey(getKey(null))
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    }

    public <T> T getClaim(String jwt, Function<Claims,T> claimsResolver) {

        Claims claims=  getAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String jwt) {
        return getClaim(jwt, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String jwt) {
        return getExpiration(jwt).before(new Date());
    }
}
