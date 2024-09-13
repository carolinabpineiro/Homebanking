package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.LoanApplicationDTO;
import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.*;
import com.Mindhubcohort55.Homebanking.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public ResponseEntity<List<LoanDto>> getLoans() {
        return ResponseEntity.ok(loanService.getLoansDTO());
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<String> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        String email = authentication.getName();
        Client client = clientService.getClientByEmail(email);
        Loan loan = loanService.findLoanById(loanApplicationDTO.getId());
        Account account = accountService.getAccountByNumber(loanApplicationDTO.getDestinationAccountNumber());

        // Verificar que el campo de cuenta no esté vacío
        if (loanApplicationDTO.getDestinationAccountNumber().isBlank()) {
            return new ResponseEntity<>("The transaction account field must not be empty", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de destino exista
        if (account == null) {
            return new ResponseEntity<>("The specified account does not exist.", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de destino pertenezca al cliente autenticado
        if (!client.getAccounts().stream().map(Account::getNumber).toList().contains(loanApplicationDTO.getDestinationAccountNumber())) {
            return new ResponseEntity<>("The specified account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        // Verificar que el tipo de préstamo exista
        if (loan == null) {
            return new ResponseEntity<>("Loan type not found.", HttpStatus.FORBIDDEN);
        }

        // Verificar que el monto no exceda el máximo permitido
        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The loan amount must be greater than 0 and less than or equal to the maximum amount allowed.", HttpStatus.FORBIDDEN);
        }

        // Verificar que las cuotas estén dentro de las permitidas para ese préstamo
        if (!loan.getPayments().contains(loanApplicationDTO.getPayment())) {
            return new ResponseEntity<>("The number of payments is not valid for the selected loan.", HttpStatus.FORBIDDEN);
        }

        // Determinar la tasa de interés según las cuotas
        double interestRate;
        if (loanApplicationDTO.getPayment() == 12) {
            interestRate = 0.20;
        } else if (loanApplicationDTO.getPayment() > 12) {
            interestRate = 0.25;
        } else {
            interestRate = 0.15;
        }

        // Agregar el 20% o la tasa variable al monto del préstamo
        double finalAmount = loanApplicationDTO.getAmount() * (1 + interestRate);

        // Crear un ClientLoan con el monto final y las cuotas
        ClientLoan clientLoan = new ClientLoan(finalAmount, loanApplicationDTO.getPayment());
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        client.getClientLoans().add(clientLoan);

        // Crear una transacción para el crédito aprobado
        Transaction creditTransaction = new Transaction(
                TransactionType.CREDIT, // Tipo de transacción
                finalAmount,            // Monto de la transacción
                "Loan approved: " + loan.getName(), // Descripción de la transacción
                LocalDateTime.now(),    // Fecha de la transacción
                account                 // Cuenta a la que se aplica la transacción
        );
        account.addTransaction(creditTransaction);

        // Actualizar el saldo de la cuenta
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());

        clientService.saveClient(client);
        accountService.saveAccount(account);

        return new ResponseEntity<>("Loan approved and credited to the account.", HttpStatus.CREATED);
    }
}
