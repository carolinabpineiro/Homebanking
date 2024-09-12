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

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDto> getLoans() {
        return loanService.getLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        String email = authentication.getName();
        Client client = clientService.getClientByEmail(email);
        Loan loan = loanService.findLoanById(loanApplicationDTO.getId());
        Account account = accountService.getAccountByNumber(loanApplicationDTO.getDestinationAccountNumber());

        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayment() <= 0) {
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

        boolean loanAlreadyRequested = client.getClientLoans().stream()
                .anyMatch(clientLoan -> clientLoan.getLoan().getId() == loan.getId());

        if (loanAlreadyRequested) {
            return new ResponseEntity<>("This kind of loan has already been requested", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.20, loanApplicationDTO.getPayment());
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        clientLoanService.saveClientLoan(clientLoan);

        if (account != null && loanApplicationDTO.getAmount() > 0) {
            Transaction creditTransaction = new Transaction(
                    TransactionType.CREDIT,
                    loanApplicationDTO.getAmount(),
                    loan.getName() + " Loan approved",
                    LocalDateTime.now(),
                    account
            );

            transactionService.makeTransaction(creditTransaction, email);

            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            accountService.saveAccount(account);

            return new ResponseEntity<>("The loan has been successfully requested", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Invalid transaction data", HttpStatus.FORBIDDEN);
        }
    }
}