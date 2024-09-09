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
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<?> makeTransaction(@RequestBody MakeTransactionDto makeTransactionDto, Authentication authentication) {

        try {
            // Obtener el cliente autenticado usando el repositorio
            Client client = clientRepository.findByEmail(authentication.getName());

            if (client == null) {
                return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
            }

            // Obtener las cuentas de origen y destino usando el repositorio
            Account sourceAccount = accountRepository.findByNumber(makeTransactionDto.sourceAccount());
            Account destinationAccount = accountRepository.findByNumber(makeTransactionDto.destinationAccount());

            // Validaciones
            if (makeTransactionDto.sourceAccount().isBlank()) {
                return new ResponseEntity<>("The source account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.destinationAccount().isBlank()) {
                return new ResponseEntity<>("The destination account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.amount() == null || makeTransactionDto.amount().isNaN() || makeTransactionDto.amount() < 0) {
                return new ResponseEntity<>("Enter a valid amount", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.description().isBlank()) {
                return new ResponseEntity<>("The description field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.sourceAccount().equals(makeTransactionDto.destinationAccount())) {
                return new ResponseEntity<>("The source account and the destination account must not be the same", HttpStatus.FORBIDDEN);
            }

            if (!accountRepository.existsByNumber(makeTransactionDto.sourceAccount())) {
                return new ResponseEntity<>("The source account entered does not exist", HttpStatus.FORBIDDEN);
            }

            if (!accountRepository.existsByIdAndOwner(sourceAccount.getId(), client)) {
                return new ResponseEntity<>("The source account entered does not belong to the client", HttpStatus.FORBIDDEN);
            }

            if (!accountRepository.existsByNumber(makeTransactionDto.destinationAccount())) {
                return new ResponseEntity<>("The destination account entered does not exist", HttpStatus.FORBIDDEN);
            }

            if (sourceAccount.getBalance() < makeTransactionDto.amount()) {
                return new ResponseEntity<>("You do not have sufficient balance to carry out the operation", HttpStatus.FORBIDDEN);
            }

            // Crear las instancias de transacciones de débito y crédito, agregarlas a sus respectivas cuentas y guardarlas en la DB
            Transaction sourceTransaction = new Transaction(TransactionType.DEBIT, -makeTransactionDto.amount(), makeTransactionDto.description() + makeTransactionDto.sourceAccount(), LocalDateTime.now(), sourceAccount);
            sourceAccount.addTransaction(sourceTransaction);
            transactionRepository.save(sourceTransaction);

            Transaction destinyTransaction = new Transaction(TransactionType.CREDIT, makeTransactionDto.amount(), makeTransactionDto.description() + makeTransactionDto.destinationAccount(), LocalDateTime.now(), destinationAccount);
            destinationAccount.addTransaction(destinyTransaction);
            transactionRepository.save(destinyTransaction);

            // Actualizar los balances de las cuentas de origen y destino, y guardar los cambios en la BD
            double sourceCurrentBalance = sourceAccount.getBalance();
            sourceAccount.setBalance(sourceCurrentBalance - makeTransactionDto.amount());
            accountRepository.save(sourceAccount);

            double destinationCurrentBalance = destinationAccount.getBalance();
            destinationAccount.setBalance(destinationCurrentBalance + makeTransactionDto.amount());
            accountRepository.save(destinationAccount);

            return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}