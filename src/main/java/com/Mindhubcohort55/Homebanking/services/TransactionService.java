package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {

    // Guarda una transacción en la base de datos.
    void saveTransaction(Transaction transaction);

    // Obtiene una transacción por su ID y la convierte a DTO.
    TransactionDto getTransactionById(Long id);

    // Obtiene todas las transacciones en formato DTO.
    List<TransactionDto> getTransactionsDTO();
}

