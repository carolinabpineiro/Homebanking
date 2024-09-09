package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    /**
     * Obtiene todos los préstamos en formato DTO.
     *
     * @return Una lista de DTOs de préstamos.
     */
    List<LoanDto> getLoansDTO();

    /**
     * Guarda un préstamo en la base de datos.
     *
     * @param loan El préstamo a guardar.
     */
    void saveLoan(Loan loan);

    /**
     * Busca un préstamo por su ID.
     *
     * @param id ID del préstamo.
     * @return El préstamo encontrado, o null si no existe.
     */
    Loan findLoanById(Long id);
}