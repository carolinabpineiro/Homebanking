package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients/current/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    // Crear una nueva cuenta para el cliente autenticado
    @PostMapping
    public ResponseEntity<?> createAccount(Authentication authentication) {
        String email = authentication.getName();
        Client client = clientService.getClientByEmail(email);

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        // Verificar si el cliente ya tiene 3 cuentas
        List<Account> accounts = accountService.getAccountsByClient(client);
        if (accounts.size() >= 3) {
            return new ResponseEntity<>("Client cannot have more than 3 accounts", HttpStatus.FORBIDDEN);
        }

        // Generar número de cuenta único
        String accountNumber = generateUniqueAccountNumber();

        // Crear una nueva cuenta con saldo 0 y cuenta activa
        Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0.0, true); // Ajuste del constructor
        client.addAccount(newAccount);
        accountService.saveAccount(newAccount); // Guardar la nueva cuenta
        clientService.saveClient(client); // Guardar el cliente con la nueva cuenta

        return new ResponseEntity<>(new AccountDto(newAccount), HttpStatus.CREATED);
    }

    // Obtener todas las cuentas del cliente autenticado
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts(Authentication authentication) {
        String email = authentication.getName();
        Client client = clientService.getClientByEmail(email);

        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Account> accounts = accountService.getAccountsByClient(client);
        List<AccountDto> accountDtos = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(accountDtos);
    }

    // Obtener una cuenta específica por su ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id) {
        Account account = accountService.getAccountById(id).orElse(null);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new AccountDto(account), HttpStatus.OK);
    }

    // Método privado para generar un número de cuenta único
    private String generateUniqueAccountNumber() {
        String leftZero;
        do {
            leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
        } while (accountService.existsByAccountNumber("VIN" + leftZero));

        return "VIN" + leftZero;
    }
}