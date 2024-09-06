package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDto> getLoansDTO();
    void saveLoan(Loan loan);
    Loan findLoanById(Long id);
}
