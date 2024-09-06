package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

    TransactionDto getTransactionById(Long id);

    List<TransactionDto> getTransactionsDTO();
}
