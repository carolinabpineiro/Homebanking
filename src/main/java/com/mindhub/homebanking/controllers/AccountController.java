package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/clients/current/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<?> createAccount(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // Suponiendo que el email es el username
        Client client = clientRepository.findByEmail(email);

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Client already has 3 accounts", HttpStatus.FORBIDDEN);
        }

        Account account = new Account();
        account.setNumber(generateRandomAccountNumber());
        account.setBalance(0.0);
        account.setCreationDate(LocalDateTime.now());
        account.setClient(client);

        Account savedAccount = accountRepository.save(account);
        client.addAccount(savedAccount);
        clientRepository.save(client);

        return new ResponseEntity<>(new AccountDTO(savedAccount), HttpStatus.CREATED);
    }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        return "VIN-" + String.format("%08d", random.nextInt(100000000));
    }
}