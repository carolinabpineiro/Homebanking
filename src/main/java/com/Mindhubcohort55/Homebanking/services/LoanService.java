package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    // Obtiene todos los préstamos en formato DTO.
    List<LoanDto> getLoansDTO();

    // Guarda un préstamo en la base de datos.
    void saveLoan(Loan loan);

    // Busca un préstamo por su ID.
    Loan findLoanById(Long id);
}