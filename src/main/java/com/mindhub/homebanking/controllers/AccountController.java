package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@CrossOrigin(origins = "http://localhost:5173") // Permitir solicitudes desde el frontend en desarrollo
@RestController
@RequestMapping("/api/accounts")

public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public List<AccountDTO> getAllTransactions() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
    }
}
