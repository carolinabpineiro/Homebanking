package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.models.TransactionType;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import com.Mindhubcohort55.Homebanking.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ClientService clientService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountService accountService,
                                  ClientService clientService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @Transactional
    @Override
    public ResponseEntity<String> makeTransaction(MakeTransactionDto makeTransactionDto, Authentication authentication) {
        ResponseEntity<String> validationResponse = validateTransaction(makeTransactionDto, authentication);
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return validationResponse;
        }

        // Realiza la transacción después de la validación
        Client client = clientService.getClientByEmail(authentication.getName());
        Account sourceAccount = accountService.getAccountByNumber(makeTransactionDto.sourceAccount());
        Account destinationAccount = accountService.getAccountByNumber(makeTransactionDto.destinationAccount());

        // Crear las transacciones
        Transaction sourceTransaction = new Transaction(
                TransactionType.DEBIT,
                makeTransactionDto.amount(),
                makeTransactionDto.description(),
                LocalDateTime.now(),
                sourceAccount
        );
        sourceAccount.addTransaction(sourceTransaction);

        Transaction destinationTransaction = new Transaction(
                TransactionType.CREDIT,
                makeTransactionDto.amount(),
                makeTransactionDto.description(),
                LocalDateTime.now(),
                destinationAccount
        );
        destinationAccount.addTransaction(destinationTransaction);

        // Actualizar balances
        sourceAccount.setBalance(sourceAccount.getBalance() - makeTransactionDto.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + makeTransactionDto.amount());

        // Guardar las transacciones y las cuentas
        transactionRepository.save(sourceTransaction);
        transactionRepository.save(destinationTransaction);
        accountService.updateAccount(sourceAccount);
        accountService.updateAccount(destinationAccount);

        return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> validateTransaction(MakeTransactionDto makeTransactionDto, Authentication authentication) {
        // Validaciones
        if (makeTransactionDto.sourceAccount().isBlank() || makeTransactionDto.destinationAccount().isBlank()) {
            return new ResponseEntity<>("Source or destination account is missing", HttpStatus.FORBIDDEN);
        }

        if (makeTransactionDto.amount() <= 0) {
            return new ResponseEntity<>("Amount must be greater than zero", HttpStatus.FORBIDDEN);
        }

        if (makeTransactionDto.description().isBlank()) {
            return new ResponseEntity<>("Description is missing", HttpStatus.FORBIDDEN);
        }

        // Obtener cliente autenticado
        Client client = clientService.getClientByEmail(authentication.getName());

        // Verificar cuentas
        Account sourceAccount = accountService.getAccountByNumber(makeTransactionDto.sourceAccount());
        Account destinationAccount = accountService.getAccountByNumber(makeTransactionDto.destinationAccount());

        if (sourceAccount == null || destinationAccount == null) {
            return new ResponseEntity<>("One or both accounts do not exist", HttpStatus.FORBIDDEN);
        }

        if (!sourceAccount.getClient().equals(client)) {
            return new ResponseEntity<>("Source account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.getBalance() < makeTransactionDto.amount()) {
            return new ResponseEntity<>("Insufficient funds in the source account", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.equals(destinationAccount)) {
            return new ResponseEntity<>("Source and destination accounts must be different", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("Validation successful", HttpStatus.OK);
    }
}
