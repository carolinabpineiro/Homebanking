package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.LoanAplicationDTO;
import com.Mindhubcohort55.Homebanking.models.*;
import com.Mindhubcohort55.Homebanking.repositories.*;
import com.Mindhubcohort55.Homebanking.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;  // Repositorio para guardar ClientLoan

    @Autowired
    private LoanServices loanServices;

    @GetMapping("/")
    public ResponseEntity<?> getLoans() {
        return ResponseEntity.ok(loanServices.getAllLoanDTO());
    }

    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> createLoan(@RequestBody LoanAplicationDTO loanAplicationDTO, Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        // Verificar que el campo de cuenta no esté vacío
        if (loanAplicationDTO.destinationAccount().isBlank()) {
            return new ResponseEntity<>("The transaction account field must not be empty", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de destino exista
        Account account = accountRepository.findByNumber(loanAplicationDTO.destinationAccount());
        if (account == null) {
            return new ResponseEntity<>("The specified account does not exist.", HttpStatus.FORBIDDEN);
        }

        // Verificar que la cuenta de destino pertenezca al cliente autenticado
        if (!client.getAccounts().stream().map(Account::getNumber).toList().contains(loanAplicationDTO.destinationAccount())) {
            return new ResponseEntity<>("The specified account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        // Verificar que el tipo de préstamo exista
        Loan loan = loanRepository.findById(loanAplicationDTO.id()).orElse(null);
        if (loan == null) {
            return new ResponseEntity<>("Loan type not found.", HttpStatus.FORBIDDEN);
        }

        // Verificar que el monto no exceda el máximo permitido
        if (loanAplicationDTO.amount() <= 0 || loanAplicationDTO.amount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The loan amount must be greater than 0 and less than or equal to the maximum amount allowed.", HttpStatus.FORBIDDEN);
        }

        // Verificar que las cuotas estén dentro de las permitidas para ese préstamo
        if (!loan.getPayments().contains(loanAplicationDTO.payments())) {
            return new ResponseEntity<>("The number of payments is not valid for the selected loan.", HttpStatus.FORBIDDEN);
        }

        // Determinar la tasa de interés según las cuotas
        double interestRate;
        if (loanAplicationDTO.payments() == 12) {
            interestRate = 0.20;
        } else if (loanAplicationDTO.payments() > 12) {
            interestRate = 0.25;
        } else {
            interestRate = 0.15;
        }

        // Agregar el 20% o la tasa variable al monto del préstamo
        double finalAmount = loanAplicationDTO.amount() * (1 + interestRate);

        // Crear un ClientLoan con el monto final y las cuotas
        ClientLoan clientLoan = new ClientLoan(finalAmount, loanAplicationDTO.payments());
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        client.getClientLoans().add(clientLoan);

        // Crear una transacción para el crédito aprobado
        Transaction creditTransaction = new Transaction(finalAmount,
                "Loan approved: " + loan.getName(),
                LocalDateTime.now(),
                TransactionType.CREDIT);
        account.addTransaction(creditTransaction);

        // Actualizar el saldo de la cuenta
        account.setBalance(account.getBalance() + loanAplicationDTO.amount());

        clientRepository.save(client);
        accountRepository.save(account);

        return new ResponseEntity<>("Loan approved and credited to the account.", HttpStatus.CREATED);
    }
}
