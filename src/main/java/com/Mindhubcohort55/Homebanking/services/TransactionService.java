package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TransactionService {
    ResponseEntity<String> makeTransaction(MakeTransactionDto makeTransactionDTO, Authentication authentication);

    ResponseEntity<String> validateTransaction(MakeTransactionDto makeTransactionDTO, Authentication authentication);

    Set<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByTransferDateBetweenAndAccountNumber(LocalDateTime dateInit, LocalDateTime dateEnd, String accountNumber);

    void saveTransaction(Transaction transaction);
}
