package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.ClientDTO;
import com.Mindhubcohort55.Homebanking.dtos.LoginDTO;
import com.Mindhubcohort55.Homebanking.dtos.RegisterDTO;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador de contraseñas para encriptar y verificar contraseñas.

    @Autowired
    private ClientRepository clientRepository; // Repositorio para manejar operaciones CRUD de clientes.

    @Autowired
    private AuthenticationManager authenticationManager; // Administrador de autenticación para manejar el proceso de autenticación.

    @Autowired
    private UserDetailsService userDetailsService; // Servicio para cargar los detalles del usuario.

    @Autowired
    private JwtUtilService jwtUtilService; // Servicio para manejar la generación y validación de JWT (JSON Web Tokens).

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Autowired
    private AccountRepository accountRepository;

    // creo datos en la api
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) { //request pone eso en el login dto
        try {
            if (loginDTO.email().isBlank()) {
                return new ResponseEntity<>("Email field cannot be empty", HttpStatus.BAD_REQUEST);
            }

            // Verifica si la contraseña cumple con el requisito mínimo de longitud.
            if (loginDTO.password().length() < 8) {
                return new ResponseEntity<>("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
            }
            // Autentica al usuario usando el email y la contraseña proporcionados.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
            // Carga los detalles del usuario después de la autenticación.
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());//verifico si existe y si existe me retorna un user details
            // Genera un token JWT para el usuario autenticado.
            final String jwt = jwtUtilService.generateToken(userDetails);
            // Retorna el token JWT en la respuesta.
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            // Retorna un error si la autenticación falla.
            return new ResponseEntity<>("Email or password invalid", HttpStatus.BAD_REQUEST);
            //agregar la rta para que el usuario no este logueado
        }
    }

    // Endpoint para registrar un nuevo cliente.
    @PostMapping("/register") // POST se utilizan para operaciones que cambian el estado del servidor, como la creación de recursos o, en este caso, el inicio de sesión, que podría considerarse como una operación que cambia el estado del sistema (creando una sesión o token de autenticación).
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) { //extraigo los valores del json y capturo los datos y los guardo en el register dto
        // Verifica si el email ya está registrado en la base de datos.
        if (clientRepository.findByEmail(registerDTO.email()) != null) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        // Verifica si el nombre y apellido no están vacíos.
        if (registerDTO.firstName().isBlank()) {
            return new ResponseEntity<>("First name field cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (registerDTO.lastName().isBlank()) {
            return new ResponseEntity<>("Last name field cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (registerDTO.password().isBlank()) {
            return new ResponseEntity<>("Password field cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (registerDTO.email().isBlank()) {
            return new ResponseEntity<>("The email field cannot be empty", HttpStatus.BAD_REQUEST);
        }

        // Verifica si la contraseña cumple con el requisito mínimo de longitud.
        if (registerDTO.password().length() < 8) {
            return new ResponseEntity<>("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
        }

        // Codifica la contraseña antes de almacenarla.
        String encodedPassword = passwordEncoder.encode(registerDTO.password());

        // Crea un nuevo cliente con la información proporcionada.
        Client client = new Client(registerDTO.firstName(), registerDTO.lastName(), registerDTO.email(), encodedPassword);

        Account newaccount = new Account(accountNumberGenerator.makeAccountNumber(), LocalDate.now(), 0.0);
        accountRepository.save(newaccount);
        client.addAccount(newaccount);


        // Guarda el nuevo cliente en la base de datos.
        clientRepository.save(client);

        // Retorna una respuesta exitosa con un mensaje de confirmación.
        return new ResponseEntity<>("Client registered successfully", HttpStatus.CREATED);
    }

    // Endpoint para obtener los detalles del cliente autenticado.
    @GetMapping("/current") //las solicitudes GET se utilizan para obtener recursos
    public ResponseEntity<?> getClient(Authentication authentication) {
        // Obtiene el cliente basado en el nombre de usuario autenticado.
        Client client = clientRepository.findByEmail(authentication.getName());
        // Retorna los detalles del cliente en la respuesta.
        return ResponseEntity.ok(new ClientDTO(client));
    }
}
