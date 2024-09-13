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
    private UserDetailsService userDetailsService; // para obtener datos del usuario.

    @Autowired
    private JwtUtilService jwtUtilService; // para crear el token


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Obtiene el encabezado de autorización de la solicitud HTTP.
            final String authorizationHeader = request.getHeader("Authorization");
            String userName = null;
            String jwt = null;

            // Verifica si el encabezado de autorización está presente y comienza con "Bearer ".
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Extrae el token JWT del encabezado (elimina el prefijo "Bearer ").
                jwt = authorizationHeader.substring(7);
                // Extrae el nombre de usuario del token JWT.
                userName = jwtUtilService.extractUserName(jwt);
            }

            // Verifica si el nombre de usuario no es nulo y si no hay una autenticación ya establecida en el contexto de seguridad.
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Carga los detalles del usuario basados en el nombre de usuario extraído.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                // Verifica si el token JWT no ha expirado.
                if (!jwtUtilService.isTokenExpired(jwt)) {
                    // Crea un objeto de autenticación con los detalles del usuario y sus autoridades.
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    // Establece los detalles de autenticación (como la solicitud HTTP) en el objeto de autenticación.
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Establece la autenticación en el contexto de seguridad.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Maneja cualquier excepción que ocurra durante el proceso de filtrado.
            // En un entorno de producción, sería más adecuado usar un logger en lugar de System.out.println.
            System.out.println(e.getMessage());
        } finally {
            // Continúa con la cadena de filtros (pasa la solicitud y la respuesta al siguiente filtro en la cadena).
            filterChain.doFilter(request, response);
        }
    }
}