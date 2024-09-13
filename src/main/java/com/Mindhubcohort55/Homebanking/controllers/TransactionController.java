package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDTO;
import com.Mindhubcohort55.Homebanking.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionServices transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<?> makeTransaction(@RequestBody MakeTransactionDTO makeTransactionDTO, Authentication authentication) {
        return transactionService.makeTransaction(makeTransactionDTO, authentication);
    }
}
