package com.Mindhubcohort55.Homebanking.controllers;
import com.Mindhubcohort55.Homebanking.dtos.AccountDTO;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")

    public class AccountController {

    @Autowired
    private ClientRepository clientRepository;


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    @GetMapping
    public List<AccountDTO> getAllTransactions() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
    }


    @PostMapping ("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        try {
            Client client = clientRepository.findByEmail(authentication.getName());

            if (client.getAccounts().size() == 3) {
                return new ResponseEntity<>("You cannot create a new account at this time. You have reached the maximum number of allowed accounts (3)", HttpStatus.FORBIDDEN);
            }

            // Genera el número de cuenta único dentro del controlador
            String accountNumber = generateUniqueAccountNumber();

            Account newAccount = new Account(accountNumber, LocalDate.now(), 0.0);
            client.addAccount(newAccount);
            accountRepository.save(newAccount);
            clientRepository.save(client);

            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @GetMapping("/clients/current/accounts")
    public ResponseEntity<?> getClientAccounts(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        List<Account> clientAccounts = accountRepository.findByOwner(client);
        List<AccountDTO> clientAccountDto = clientAccounts.stream().map(AccountDTO::new).collect(Collectors.toList());

        return new ResponseEntity<>(clientAccountDto, HttpStatus.OK );
    }

    // Método privado para generar un número de cuenta único
    private String generateUniqueAccountNumber() {
        String leftZero;
        do {
            leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
        } while (accountRepository.existsByAccountNumber("VIN" + leftZero));

        return "VIN" + leftZero;
    }

}
