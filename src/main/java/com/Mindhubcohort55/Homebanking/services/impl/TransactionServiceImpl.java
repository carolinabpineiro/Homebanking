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
    public void makeTransaction(MakeTransactionDto makeTransactionDto, String clientEmail) {
        // Validaciones
        if (makeTransactionDto.sourceAccount().isBlank() || makeTransactionDto.destinationAccount().isBlank()) {
            throw new IllegalArgumentException("Source or destination account is missing");
        }

        if (makeTransactionDto.amount() == null || makeTransactionDto.amount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (makeTransactionDto.description().isBlank()) {
            throw new IllegalArgumentException("Description is missing");
        }

        // Obtener cliente autenticado
        Client client = clientService.getClientByEmail(clientEmail);

        // Verificar cuentas
        Account sourceAccount = accountService.getAccountByNumber(makeTransactionDto.sourceAccount());
        Account destinationAccount = accountService.getAccountByNumber(makeTransactionDto.destinationAccount());

        if (!sourceAccount.getClient().equals(client)) {
            throw new IllegalArgumentException("Source account does not belong to the authenticated client");
        }

        if (sourceAccount.getBalance() < makeTransactionDto.amount()) {
            throw new IllegalArgumentException("Insufficient funds in the source account");
        }

        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        // Crear las transacciones
        Transaction sourceTransaction = new Transaction(
                TransactionType.DEBIT,
                -makeTransactionDto.amount(),
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
    }
}

