package com.mindhub.homebanking.servicesSecurity;


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

    // Definición de la clave secreta utilizada para firmar los tokens JWT.
    // En un entorno de producción, esta clave debería provenir de una fuente segura como una variable de entorno o un archivo de configuración.
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    // Tiempo de validez del token JWT en milisegundos (1 hora).
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hora

    /**
     * Extrae todos los reclamos (claims) del token JWT.
     *
     * @param token El token JWT del que se extraen los reclamos.
     * @return Los reclamos extraídos del token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Extrae un reclamo específico del token JWT utilizando una función proporcionada.
     *
     * @param <T> La tipo de dato del reclamo a extraer.
     * @param token El token JWT del que se extrae el reclamo.
     * @param claimsTFunction Función que define cómo extraer el reclamo deseado.
     * @return El valor del reclamo extraído.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token El token JWT del que se extrae el nombre de usuario.
     * @return El nombre de usuario extraído.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token El token JWT del que se extrae la fecha de expiración.
     * @return La fecha de expiración del token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Verifica si el token JWT ha expirado.
     *
     * @param token El token JWT que se verifica.
     * @return true si el token ha expirado, false en caso contrario.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Crea un nuevo token JWT con los reclamos y el nombre de usuario proporcionados.
     *
     * @param claims Los reclamos que se incluirán en el token.
     * @param username El nombre de usuario que se establecerá en el token.
     * @return El token JWT generado como una cadena compacta.
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims) // Agrega los reclamos al token.
                .setSubject(username) // Establece el sujeto (nombre de usuario) del token.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // Establece la fecha de expiración del token.
                .signWith(SECRET_KEY) // Firma el token con la clave secreta.
                .compact(); // Construye el token JWT como una cadena compacta.
    }

    /**
     * Genera un nuevo token JWT para un usuario dado.
     *
     * @param userDetails Los detalles del usuario para el que se genera el token.
     * @return El token JWT generado.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Obtiene el rol del usuario y lo agrega a los reclamos.
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("rol", rol);
        // Crea y retorna el token JWT con los reclamos y el nombre de usuario del usuario.
        return createToken(claims, userDetails.getUsername());
    }
}