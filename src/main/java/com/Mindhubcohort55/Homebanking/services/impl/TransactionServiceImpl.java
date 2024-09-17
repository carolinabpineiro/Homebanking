package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
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
import java.util.List;
import java.util.Set;
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @Override
    public ResponseEntity<String> makeTransaction(MakeTransactionDto makeTransactionDto, Authentication authentication) {
        // Logs para depurar
        System.out.println("Authentication Name: " + authentication.getName());
        System.out.println("Transaction DTO: " + makeTransactionDto);

        // Validación de la transacción
        ResponseEntity<String> validationResponse = validateTransaction(makeTransactionDto, authentication);
        if (validationResponse.getStatusCode() != HttpStatus.OK) {
            return validationResponse;
        }

        // Realiza la transacción
        Client client = clientService.getClientByEmail(authentication.getName());
        System.out.println("Authenticated Client: " + client);

        Account sourceAccount = accountService.getAccountByNumber(makeTransactionDto.sourceAccount());
        Account destinationAccount = accountService.getAccountByNumber(makeTransactionDto.destinationAccount());

        System.out.println("Source Account: " + sourceAccount);
        System.out.println("Destination Account: " + destinationAccount);

        // Validación de cuentas
        if (sourceAccount == null || destinationAccount == null) {
            return new ResponseEntity<>("One or both accounts do not exist", HttpStatus.FORBIDDEN);
        }

        // Validación de propiedad de la cuenta de origen
        if (!client.ownsAccount(sourceAccount)) {
            return new ResponseEntity<>("Source account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }

        // Verificación del estado activo de las cuentas
        if (!sourceAccount.isStatus() || !destinationAccount.isStatus()) {
            return new ResponseEntity<>("One or both accounts are inactive", HttpStatus.FORBIDDEN);
        }

        // Validación de saldo
        if (sourceAccount.getBalance() < makeTransactionDto.amount()) {
            return new ResponseEntity<>("Insufficient funds in the source account", HttpStatus.FORBIDDEN);
        }

        // Validación de cuentas diferentes
        if (sourceAccount.equals(destinationAccount)) {
            return new ResponseEntity<>("Source and destination accounts must be different", HttpStatus.FORBIDDEN);
        }

        // Crear y guardar transacciones
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

        // Guardar transacciones y cuentas
        transactionRepository.save(sourceTransaction);
        transactionRepository.save(destinationTransaction);
        accountService.updateAccount(sourceAccount);
        accountService.updateAccount(destinationAccount);

        return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> validateTransaction(MakeTransactionDto makeTransactionDto, Authentication authentication) {
        // Agrega logs para depurar
        System.out.println("Validating transaction with DTO: " + makeTransactionDto);

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
        System.out.println("Client for validation: " + client);

        // Verificar cuentas
        Account sourceAccount = accountService.getAccountByNumber(makeTransactionDto.sourceAccount());
        Account destinationAccount = accountService.getAccountByNumber(makeTransactionDto.destinationAccount());

        System.out.println("Source Account for validation: " + sourceAccount);
        System.out.println("Destination Account for validation: " + destinationAccount);

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

    @Override
    public Set<Transaction> findByAccountId(Long accountId) {
        // Obtén la cuenta por ID y luego encuentra las transacciones asociadas
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccount(account);
    }

    @Override
    public List<Transaction> findByTransferDateBetweenAndAccountNumber(LocalDateTime dateInit, LocalDateTime dateEnd, String accountNumber) {
        return transactionRepository.findByDateBetweenAndAccountNumber(dateInit, dateEnd, accountNumber);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}