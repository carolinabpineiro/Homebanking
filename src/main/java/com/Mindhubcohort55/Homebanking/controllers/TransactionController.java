package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
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
import com.Mindhubcohort55.Homebanking.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @PostMapping
    public ResponseEntity<String> makeTransaction(@RequestBody MakeTransactionDto makeTransactionDto, Authentication authentication) {
        // Validaciones
        if (makeTransactionDto.sourceAccount().isBlank() || makeTransactionDto.destinationAccount().isBlank()) {
            return new ResponseEntity<>("Source or destination account is missing", HttpStatus.FORBIDDEN);
        }

        if (makeTransactionDto.amount() == null || makeTransactionDto.amount() <= 0) {
            return new ResponseEntity<>("Amount must be greater than zero", HttpStatus.FORBIDDEN);
        }

        if (makeTransactionDto.description().isBlank()) {
            return new ResponseEntity<>("Description is missing", HttpStatus.FORBIDDEN);
        }

        // Obtener el cliente autenticado
        ClientDto clientDto = clientService.getClientCurrent(authentication);
        if (clientDto == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }

        // Verificar las cuentas
        Account sourceAccount = accountService.getAccountByNumber(makeTransactionDto.sourceAccount());
        Account destinationAccount = accountService.getAccountByNumber(makeTransactionDto.destinationAccount());

        if (sourceAccount == null || destinationAccount == null) {
            return new ResponseEntity<>("One or both accounts do not exist", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de origen pertenezca al cliente autenticado
        if (!sourceAccount.getClient().getEmail().equals(clientDto.getEmail())) {
            return new ResponseEntity<>("Source account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.getBalance() < makeTransactionDto.amount()) {
            return new ResponseEntity<>("Insufficient funds in the source account", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.equals(destinationAccount)) {
            return new ResponseEntity<>("Source and destination accounts must be different", HttpStatus.FORBIDDEN);
        }

        // Realizar la transacción
        try {
            transactionService.makeTransaction(makeTransactionDto, clientDto.getEmail());
            return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}