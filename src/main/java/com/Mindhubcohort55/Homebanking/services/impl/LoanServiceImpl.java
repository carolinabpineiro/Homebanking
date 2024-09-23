package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.LoanApplicationDto;
import com.Mindhubcohort55.Homebanking.dtos.LoanDto;
import com.Mindhubcohort55.Homebanking.models.*;
import com.Mindhubcohort55.Homebanking.repositories.*;
import com.Mindhubcohort55.Homebanking.services.LoanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<LoanDto> getLoansDTO() {
        return loanRepository.findAll().stream()
                .map(LoanDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public ResponseEntity<?> applyForLoan(String email, LoanApplicationDto loanApplicationDto) {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        Loan loan = getLoanById(loanApplicationDto.getId());
        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }

        Account account = accountRepository.findByNumber(loanApplicationDto.getDestinationAccount());
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        // Verificar si el cliente ya ha solicitado este préstamo
        if (clientHasAppliedForLoan(client, loan)) {
            return new ResponseEntity<>("You have already applied for this loan", HttpStatus.BAD_REQUEST);
        }

        String validationError = validateLoanApplication(loanApplicationDto, client, loan);
        if (validationError != null) {
            return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
        }

        // Monto solicitado por el cliente
        double requestedAmount = loanApplicationDto.getAmount();

        // Calcular el monto total a devolver (incluyendo intereses)
        double interestRate = determineInterestRate(loanApplicationDto.getPayments());
        double totalAmountToRepay = requestedAmount * (1 + interestRate); // Total a devolver

        ClientLoan clientLoan = new ClientLoan(totalAmountToRepay, loanApplicationDto.getPayments());
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);

        Transaction transaction = new Transaction(
                TransactionType.CREDIT,
                requestedAmount, // Acreditar solo el monto solicitado
                "Loan approved: " + loan.getName(),
                LocalDateTime.now(),
                account
        );

        // Actualiza el balance de la cuenta
        account.setBalance(account.getBalance() + requestedAmount);
        account.addTransaction(transaction);
        client.getClientLoans().add(clientLoan);

        clientLoanRepository.save(clientLoan);
        transactionRepository.save(transaction);
        clientRepository.save(client);
        accountRepository.save(account); // Guardar la cuenta con el nuevo balance

        return new ResponseEntity<>("Loan approved and credited to the account", HttpStatus.CREATED);
    }

    private double determineInterestRate(int payments) {
        if (payments == 12) return 0.20;
        if (payments > 12) return 0.25;
        return 0.15;
    }

    private String validateLoanApplication(LoanApplicationDto dto, Client client, Loan loan) {
        if (dto.getAmount() <= 0 || dto.getAmount() > loan.getMaxAmount()) return "Invalid amount";
        if (!loan.getPayments().contains(dto.getPayments())) return "Invalid payments";
        return null;
    }

    private boolean clientHasAppliedForLoan(Client client, Loan loan) {
        return client.getClientLoans().stream().anyMatch(cl -> cl.getLoan().equals(loan));
    }


}