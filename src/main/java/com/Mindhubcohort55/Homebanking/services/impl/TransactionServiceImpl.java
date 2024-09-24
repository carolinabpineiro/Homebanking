package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.models.TransactionType;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
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
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @Override
    public ResponseEntity<String> makeTransaction(MakeTransactionDto makeTransactionDTO, Authentication authentication) {
        // Implementa la lógica para realizar una transacción basada en el DTO
        Client client = clientRepository.findByEmail(authentication.getName());
        Account sourceAccount = accountRepository.findByNumber(makeTransactionDTO.sourceAccount());
        Account destinationAccount = accountRepository.findByNumber(makeTransactionDTO.destinationAccount());

        if (client == null || sourceAccount == null || destinationAccount == null) {
            return new ResponseEntity<>("Invalid account or client", HttpStatus.BAD_REQUEST);
        }

        if (!client.getAccounts().contains(sourceAccount)) {
            return new ResponseEntity<>("Client does not own the source account", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.getBalance() < makeTransactionDTO.amount()) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.equals(destinationAccount)) {
            return new ResponseEntity<>("Source and destination accounts must be different", HttpStatus.FORBIDDEN);
        }

        // Crear y guardar transacciones
        Transaction sourceTransaction = new Transaction(
                TransactionType.DEBIT,
                makeTransactionDTO.amount(),
                makeTransactionDTO.description(),
                LocalDateTime.now(),
                sourceAccount
        );
        sourceAccount.addTransaction(sourceTransaction);

        Transaction destinationTransaction = new Transaction(
                TransactionType.CREDIT,
                makeTransactionDTO.amount(),
                makeTransactionDTO.description(),
                LocalDateTime.now(),
                destinationAccount
        );
        destinationAccount.addTransaction(destinationTransaction);

        // Actualizar balances
        sourceAccount.setBalance(sourceAccount.getBalance() - makeTransactionDTO.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + makeTransactionDTO.amount());

        // Guardar transacciones y cuentas
        transactionRepository.save(sourceTransaction);
        transactionRepository.save(destinationTransaction);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> validateTransaction(MakeTransactionDto makeTransactionDTO, Authentication authentication) {
        // Validaciones básicas
        if (makeTransactionDTO.sourceAccount().isBlank() || makeTransactionDTO.destinationAccount().isBlank()) {
            return new ResponseEntity<>("Source or destination account is missing", HttpStatus.FORBIDDEN);
        }

        if (makeTransactionDTO.amount() <= 0) {
            return new ResponseEntity<>("Amount must be greater than zero", HttpStatus.FORBIDDEN);
        }

        if (makeTransactionDTO.description().isBlank()) {
            return new ResponseEntity<>("Description is missing", HttpStatus.FORBIDDEN);
        }

        // Validación adicional
        Client client = clientRepository.findByEmail(authentication.getName());
        Account sourceAccount = accountRepository.findByNumber(makeTransactionDTO.sourceAccount());
        Account destinationAccount = accountRepository.findByNumber(makeTransactionDTO.destinationAccount());

        if (sourceAccount == null || destinationAccount == null) {
            return new ResponseEntity<>("One or both accounts do not exist", HttpStatus.FORBIDDEN);
        }

        if (!sourceAccount.getClient().equals(client)) {
            return new ResponseEntity<>("Source account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.getBalance() < makeTransactionDTO.amount()) {
            return new ResponseEntity<>("Insufficient funds in the source account", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.equals(destinationAccount)) {
            return new ResponseEntity<>("Source and destination accounts must be different", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("Validation successful", HttpStatus.OK);
    }

    @Override
    public Set<Transaction> findByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
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



    @Transactional
    @Override
    public ResponseEntity<String> transferFunds(String email, String sourceAccountNumber, String destinationAccountNumber, double amount) {
        try {
            Client client = clientRepository.findByEmail(email);
            Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
            Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

            if (client == null || sourceAccount == null || destinationAccount == null) {
                return new ResponseEntity<>("Invalid account or client", HttpStatus.BAD_REQUEST);
            }

            if (!client.getAccounts().contains(sourceAccount)) {
                return new ResponseEntity<>("Client does not own the source account", HttpStatus.FORBIDDEN);
            }

            // Additional logic for transferring funds
            if (sourceAccount.getBalance() < amount) {
                return new ResponseEntity<>("Insufficient funds in source account", HttpStatus.FORBIDDEN);
            }

            // Create and save transactions
            Transaction sourceTransaction = new Transaction(
                    TransactionType.DEBIT,
                    amount,
                    "Transfer to " + destinationAccountNumber,
                    LocalDateTime.now(),
                    sourceAccount
            );
            Transaction destinationTransaction = new Transaction(
                    TransactionType.CREDIT,
                    amount,
                    "Transfer from " + sourceAccountNumber,
                    LocalDateTime.now(),
                    destinationAccount
            );

            sourceAccount.setBalance(sourceAccount.getBalance() - amount);
            destinationAccount.setBalance(destinationAccount.getBalance() + amount);

            transactionRepository.save(sourceTransaction);
            transactionRepository.save(destinationTransaction);
            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

            return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        // Obtener la cuenta usando el ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Obtener las transacciones asociadas a la cuenta
        Set<Transaction> transactions = transactionRepository.findByAccount(account);

        // Convertir las transacciones a TransactionDto
        return transactions.stream()
                .map(TransactionDto::new) // Utiliza el constructor que acepta Transaction
                .collect(Collectors.toList());
    }

}