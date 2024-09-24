package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.LoanApplicationDto;
import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.Loan;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanService {
    List<LoanDto> getLoansDTO();
    void saveLoan(Loan loan);
    Loan findLoanById(Long id);

    Loan getLoanById(Long id);


    @Transactional
    ResponseEntity<?> applyForLoan(String email, LoanApplicationDto loanApplicationDto);


}