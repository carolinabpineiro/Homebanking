package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.dtos.LoginDto;
import com.Mindhubcohort55.Homebanking.dtos.RegisterDto;
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
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        try {

            if (loginDto.email().isBlank()) {
                return new ResponseEntity<>("The Email field must noy be empty", HttpStatus.BAD_REQUEST);
            }

            if (loginDto.password().isBlank()) {
                return new ResponseEntity<>("The Password field must noy be empty", HttpStatus.BAD_REQUEST);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.email());
            final String jwt = jwtUtilService.generateToken(userDetails);
            return ResponseEntity.ok(jwt);
        }
        catch (Exception e){
            return new ResponseEntity<>("Sorry, email or password invalid", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        try{
            if(registerDto.firstName().isBlank()){
                return new ResponseEntity<>("The Name field must noy be empty", HttpStatus.BAD_REQUEST);
            }

            if(registerDto.lastName().isBlank()){
                return new ResponseEntity<>("The Last Name field must not be empty", HttpStatus.BAD_REQUEST);
            }

            if(registerDto.password().isBlank()){
                return new ResponseEntity<>("The Password field must not be empty", HttpStatus.BAD_REQUEST);
            }

            if(registerDto.email().isBlank()){
                return new ResponseEntity<>("The Email field must not be empty", HttpStatus.FORBIDDEN);
            }

            if(clientRepository.existsByEmail(registerDto.email())){
                return new ResponseEntity<>("The Email entered is already registered", HttpStatus.FORBIDDEN);
            }

            Client newClient = new Client(registerDto.firstName(), registerDto.lastName(), registerDto.email(), passwordEncoder.encode(registerDto.password()));
            Account defaultaccount = new Account(AccountNumberGenerator.makeAccountNumber(), LocalDateTime.now(), 0.00);
            newClient.addAccounts(defaultaccount);

            clientRepository.save(newClient);
            accountRepository.save(defaultaccount);

            return new ResponseEntity<>("Client created", HttpStatus.CREATED);
        }

        catch (Exception e) {
            return new ResponseEntity<>("Error creating client: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return ResponseEntity.ok(new ClientDto(client));
    }
}