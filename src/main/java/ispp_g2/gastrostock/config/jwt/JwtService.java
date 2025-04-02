package ispp_g2.gastrostock.config.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {


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
            .setId(String.valueOf((new Random()).nextInt(100)))
            .signWith(getKey(),SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getKey() {
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
            .setSigningKey(getKey())
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
