package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.TransactionDto;
import com.Mindhubcohort55.Homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {

    /**
     * Guarda una transacción en la base de datos.
     *
     * @param transaction La transacción a guardar.
     */
    void saveTransaction(Transaction transaction);

    /**
     * Obtiene una transacción por su ID y la convierte a DTO.
     *
     * @param id ID de la transacción.
     * @return El DTO de la transacción encontrada, o null si no existe.
     */
    TransactionDto getTransactionById(Long id);

    /**
     * Obtiene todas las transacciones en formato DTO.
     *
     * @return Una lista de DTOs de transacciones.
     */
    List<TransactionDto> getTransactionsDTO();
}

