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

    // Repositorio para gestionar operaciones sobre transacciones
    @Autowired
    TransactionRepository transactionRepository;

    /**
     * Guarda una transacción en la base de datos.
     *
     * @param transaction La transacción a guardar.
     */
    @Override
    public void saveTransaction(Transaction transaction) {
        // Guardar la transacción en la base de datos usando el repositorio
        transactionRepository.save(transaction);
    }

    /**
     * Obtiene una transacción por su ID y la convierte a DTO.
     *
     * @param id ID de la transacción.
     * @return DTO de la transacción encontrada, o null si no existe.
     */
    @Override
    public TransactionDto getTransactionById(Long id) {
        // Buscar la transacción por ID y mapearla a un DTO, o devolver null si no existe
        return transactionRepository.findById(id).map(transaction -> new TransactionDto(transaction)).orElse(null);
    }

    /**
     * Obtiene todas las transacciones y las convierte a DTOs.
     *
     * @return Lista de DTOs de transacciones.
     */
    @Override
    public List<TransactionDto> getTransactionsDTO() {
        // Obtener todas las transacciones, mapearlas a DTOs y recogerlas en una lista
        return transactionRepository.findAll().stream().map(TransactionDto::new).collect(Collectors.toList());
    }
}