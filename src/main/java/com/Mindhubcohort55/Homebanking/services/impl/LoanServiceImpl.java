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

    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<LoanDto> getLoansDTO() {
        return loanRepository.findAll().stream().map(LoanDto::new).collect(Collectors.toList());
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }
}
