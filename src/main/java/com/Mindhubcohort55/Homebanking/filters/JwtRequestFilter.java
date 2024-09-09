package com.Mindhubcohort55.Homebanking.filters;

import com.Mindhubcohort55.Homebanking.servicesSecurity.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7); // Extrae el JWT del encabezado
                username = jwtUtilService.extractUserName(jwt); // Extrae el nombre de usuario del JWT
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Carga los detalles del usuario

                if (!jwtUtilService.isTokenExpired(jwt)) { // Verifica si el token ha expirado
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // Crea un objeto de autenticación con los detalles del usuario
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Agrega detalles adicionales a la autenticación
                    SecurityContextHolder.getContext().setAuthentication(authentication); // Establece la autenticación en el contexto de seguridad
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Manejo de errores, imprime el mensaje de excepción
        } finally {
            filterChain.doFilter(request, response); // Continua con el filtro en la cadena
        }
    }
}