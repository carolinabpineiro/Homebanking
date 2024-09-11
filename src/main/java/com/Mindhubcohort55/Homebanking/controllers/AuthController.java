package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.dtos.LoginDto;
import com.Mindhubcohort55.Homebanking.dtos.RegisterDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.servicesSecurity.JwtUtilService;
import com.Mindhubcohort55.Homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService; // Usa el servicio de cuentas en lugar de AccountRepository directamente

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            if (loginDto.email().isBlank()) {
                return new ResponseEntity<>("The Email field must not be empty", HttpStatus.BAD_REQUEST);
            }

            if (loginDto.password().isBlank()) {
                return new ResponseEntity<>("The Password field must not be empty", HttpStatus.BAD_REQUEST);
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.email());
            final String jwt = jwtUtilService.generateToken(userDetails);

            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return new ResponseEntity<>("Sorry, email or password invalid", HttpStatus.FORBIDDEN);
        }
    }

    // Registro
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterDto registerDto) {
        try {
            // Validaciones de campos vacíos
            if (registerDto.firstName().isBlank()) {
                return new ResponseEntity<>("The Name field must not be empty", HttpStatus.BAD_REQUEST);
            }

            if (registerDto.lastName().isBlank()) {
                return new ResponseEntity<>("The Last Name field must not be empty", HttpStatus.BAD_REQUEST);
            }

            if (registerDto.password().isBlank()) {
                return new ResponseEntity<>("The Password field must not be empty", HttpStatus.BAD_REQUEST);
            }

            if (registerDto.email().isBlank()) {
                return new ResponseEntity<>("The Email field must not be empty", HttpStatus.BAD_REQUEST);
            }

            // Verificación de existencia de email
            if (clientRepository.existsByEmail(registerDto.email())) {
                return new ResponseEntity<>("The Email entered is already registered", HttpStatus.FORBIDDEN);
            }

            // Creación del nuevo cliente
            Client newClient = new Client(
                    registerDto.firstName(),
                    registerDto.lastName(),
                    registerDto.email(),
                    passwordEncoder.encode(registerDto.password())
            );

            // Creación de la cuenta por defecto para el cliente
            Account defaultAccount = accountService.createDefaultAccountForNewClient(); // Método de AccountService

            // Asignar la cuenta al cliente
            newClient.addAccount(defaultAccount);

            // Guardar cliente con su nueva cuenta
            clientRepository.save(newClient);

            return new ResponseEntity<>("Client created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating client: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener cliente actual
    @GetMapping("/current")
    public ResponseEntity<?> getClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new ClientDto(client));
    }
}