package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
import com.Mindhubcohort55.Homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return transactionRepository.findById(id).map(transaction -> new TransactionDto(transaction)).orElse(null);
    }

    @Override
    public List<TransactionDto> getTransactionsDTO() {
        return transactionRepository.findAll().stream().map(TransactionDto::new).collect(Collectors.toList());
    }
}
