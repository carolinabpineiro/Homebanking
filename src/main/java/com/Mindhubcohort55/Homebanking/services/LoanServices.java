package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.LoanAplicationDTO;
import com.Mindhubcohort55.Homebanking.dtos.LoanApplicationDTO;
import com.Mindhubcohort55.Homebanking.dtos.LoanDTO;
import com.Mindhubcohort55.Homebanking.models.Loan;
import org.springframework.security.core.Authentication;


import java.util.List;


public interface LoanServices {
    List<Loan> getAllLoans();
    List<LoanDTO> getAllLoanDTO();
    Loan applyLoan(Authentication authentication, LoanAplicationDTO loanDTO);  // Asegúrate de que esta firma sea la que quieres usar
}
