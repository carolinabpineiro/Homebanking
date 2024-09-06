package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.models.TransactionType;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
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

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<String> makeTransaction(@RequestBody MakeTransactionDto makeTransactionDto, Authentication authentication) {
        try {
            // Obtener el cliente autenticado
            Client client = clientRepository.findByEmail(authentication.getName());

            if (client == null) {
                return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
            }

            // Obtener las cuentas de origen y destino
            Account sourceAccount = accountRepository.findByNumber(makeTransactionDto.sourceAccount());
            Account destinationAccount = accountRepository.findByNumber(makeTransactionDto.destinationAccount());

            // Validaciones
            if (makeTransactionDto.sourceAccount().isBlank()) {
                return new ResponseEntity<>("The source account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.destinationAccount().isBlank()) {
                return new ResponseEntity<>("The destination account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.amount() == null || makeTransactionDto.amount() <= 0) {
                return new ResponseEntity<>("Enter a valid amount", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.description().isBlank()) {
                return new ResponseEntity<>("The description field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.sourceAccount().equals(makeTransactionDto.destinationAccount())) {
                return new ResponseEntity<>("The source account and the destination account must not be the same", HttpStatus.FORBIDDEN);
            }

            if (sourceAccount == null || !accountRepository.existsByIdAndOwner(sourceAccount.getId(), client)) {
                return new ResponseEntity<>("The source account entered does not belong to the client or does not exist", HttpStatus.FORBIDDEN);
            }

            if (destinationAccount == null) {
                return new ResponseEntity<>("The destination account entered does not exist", HttpStatus.FORBIDDEN);
            }

            if (sourceAccount.getBalance() < makeTransactionDto.amount()) {
                return new ResponseEntity<>("You do not have sufficient balance to carry out the operation", HttpStatus.FORBIDDEN);
            }

            // Crear las instancias de transacciones de débito y crédito
            Transaction sourceTransaction = new Transaction(TransactionType.DEBIT, -makeTransactionDto.amount(), makeTransactionDto.description(), LocalDateTime.now(), sourceAccount);
            sourceAccount.addTransaction(sourceTransaction);
            transactionRepository.save(sourceTransaction);

            Transaction destinationTransaction = new Transaction(TransactionType.CREDIT, makeTransactionDto.amount(), makeTransactionDto.description(), LocalDateTime.now(), destinationAccount);
            destinationAccount.addTransaction(destinationTransaction);
            transactionRepository.save(destinationTransaction);

            // Actualizar balances de las cuentas
            sourceAccount.setBalance(sourceAccount.getBalance() - makeTransactionDto.amount());
            destinationAccount.setBalance(destinationAccount.getBalance() + makeTransactionDto.amount());
            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

            return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}