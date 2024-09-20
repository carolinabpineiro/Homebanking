package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import com.Mindhubcohort55.Homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService; // Inyectar ClientService

    @Autowired
    private AccountService AccountService; // Inyectar AccountService

    @PostMapping("/transactions")
    public ResponseEntity<String> makeTransaction(@RequestBody MakeTransactionDto makeTransactionDto, Authentication authentication) {
        return transactionService.makeTransaction(makeTransactionDto, authentication);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountId(@PathVariable Long id, Authentication authentication) {
        // Obtener el cliente autenticado
        Client client = clientService.getClientByEmail(authentication.getName());

        // Verificar si la cuenta pertenece al cliente
        Optional<Account> accountOptional = AccountService.getAccountById(id);
        if (!accountOptional.isPresent() || !accountOptional.get().getClient().equals(client)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 si no es el propietario
        }

        // Obtener las transacciones de la cuenta usando la instancia de transactionService
        List<TransactionDto> transactions = transactionService.getTransactionsByAccountId(id);
        return ResponseEntity.ok(transactions);
    }
}