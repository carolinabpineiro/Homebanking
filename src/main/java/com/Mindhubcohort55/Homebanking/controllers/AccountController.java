package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.models.TransactionType;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients/current")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/accounts")
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

        Account newAccount = accountService.createDefaultAccount(client.getId());
        return new ResponseEntity<>(new AccountDto(newAccount), HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
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
}