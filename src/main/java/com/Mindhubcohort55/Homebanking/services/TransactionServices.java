package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface TransactionServices {
    ResponseEntity<?> makeTransaction(MakeTransactionDTO makeTransactionDTO, Authentication authentication);
}

