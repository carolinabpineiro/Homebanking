package com.Mindhubcohort55.Homebanking.servicesSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();


    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }


    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public String createToken(Map<String, Object> parclavevalor, String username) { //par llave valor
        return Jwts
                .builder()
                .claims(parclavevalor)
                .setSubject(username) // Establece el sujeto (nombre de usuario) del token.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // Establece la fecha de expiración del token.
                .signWith(SECRET_KEY) // Firma el token con la clave secreta.
                .compact(); // Construye el token JWT como una cadena compacta.
    }


    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Obtiene el rol del usuario y lo agrega a los reclamos.
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();//por si tiene + de un rol
        claims.put("rol", rol);
        // Crea y retorna el token JWT con los reclamos y el nombre de usuario del usuario.
        return createToken(claims, userDetails.getUsername());
    }
}
