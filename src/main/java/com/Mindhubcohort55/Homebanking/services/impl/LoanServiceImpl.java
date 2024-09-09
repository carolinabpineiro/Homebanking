package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.Loan;
import com.Mindhubcohort55.Homebanking.repositories.LoanRepository;
import com.Mindhubcohort55.Homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    // Repositorio para gestionar operaciones sobre préstamos
    @Autowired
    LoanRepository loanRepository;

    /**
     * Obtiene todos los préstamos y los convierte a DTOs.
     *
     * @return Una lista de DTOs de préstamos.
     */
    @Override
    public List<LoanDto> getLoansDTO() {
        // Obtener todos los préstamos, mapearlos a DTOs y recogerlos en una lista
        return loanRepository.findAll().stream().map(LoanDto::new).collect(Collectors.toList());
    }

    /**
     * Guarda un préstamo en la base de datos.
     *
     * @param loan El préstamo a guardar.
     */
    @Override
    public void saveLoan(Loan loan) {
        // Guardar el préstamo en la base de datos usando el repositorio
        loanRepository.save(loan);
    }

    /**
     * Busca un préstamo por su ID.
     *
     * @param id ID del préstamo.
     * @return El préstamo encontrado, o null si no existe.
     */
    @Override
    public Loan findLoanById(Long id) {
        // Buscar el préstamo por ID y devolverlo, o devolver null si no existe
        return loanRepository.findById(id).orElse(null);
    }
}
