package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.LoanApplicationDTO;
import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.*;
import com.Mindhubcohort55.Homebanking.services.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


public class LoanController {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    LoanService loanService;
    @Autowired
    ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDto> getLoans() {
        return loanService.getLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        Loan loan = loanService.findLoanById(loanApplicationDTO.getId());
        Account account = accountService.getAccountByNumber(loanApplicationDTO.getDestinationAccountNumber());
        Client client = clientService.getClientCurrent(authentication);

        if (loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayment() == 0) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (loan == null) {
            return new ResponseEntity<>("This kind of loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The amount requested is greater than the amount allowed", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayment())) {
            return new ResponseEntity<>("The number of payments is not allowed in this type of loan", HttpStatus.FORBIDDEN);
        }

        if (account == null) {
            return new ResponseEntity<>("The destination account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("The destination account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        // Verificación si el cliente ya solicitó este tipo de préstamo
        boolean loanAlreadyRequested = client.getClientLoans().stream()
                .anyMatch(clientLoan -> clientLoan.getLoan().getId() == loan.getId());

        if (loanAlreadyRequested) {
            return new ResponseEntity<>("This kind of loan has already been requested", HttpStatus.FORBIDDEN);
        }

        // Creación del nuevo ClientLoan
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.20, loanApplicationDTO.getPayment());
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        clientLoanService.saveClientLoan(clientLoan);

        // Creación de la transacción con TransactionType correctamente asignado
        Transaction creditTransaction = new Transaction(
                TransactionType.CREDIT,                  // Tipo de transacción
                loanApplicationDTO.getAmount(),          // Monto de la transacción
                loan.getName() + " Loan approved",      // Descripción
                LocalDateTime.now(),                    // Fecha de la transacción
                account                                // Cuenta asociada
        );

        // Actualización del saldo de la cuenta
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountService.saveAccount(account);

        return new ResponseEntity<>("The loan has been successfully requested", HttpStatus.CREATED);
    }
}