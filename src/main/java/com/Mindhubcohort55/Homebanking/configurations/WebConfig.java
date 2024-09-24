package com.Mindhubcohort55.Homebanking.configurations;

import com.Mindhubcohort55.Homebanking.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth/login", "/api/auth/signup", "/h2-console/**").permitAll()
                                .requestMatchers(
                                        "/api/accounts/current",
                                        "/api/accounts/{id}",
                                        "/api/auth/current",
                                        "/api/clients/current/cards",
                                        "/api/loans/",
                                        "/api/loans/apply",
                                        "/api/loans/loansAvailable",
                                        "/api/loans/loansApplied",
                                        "/api/transactions",
                                        "/api/transactions/{id}"

                                ).hasRole("CLIENT")
                                .requestMatchers("/api/clients/",
                                        "/api/clients/**",
                                        "/api/accounts/",
                                        "/api/accounts/**",
                                        "/api/cards/",
                                        "/api/cards/**",
                                        "/api/loans/",
                                        "/api/loans/**").hasRole("ADMIN")
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}