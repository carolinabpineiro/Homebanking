package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    void makeTransaction(MakeTransactionDto makeTransactionDto, String clientEmail);
}