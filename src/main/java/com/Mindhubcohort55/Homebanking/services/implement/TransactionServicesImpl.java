package com.Mindhubcohort55.Homebanking.services.implement;

import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDTO;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.models.TransactionType;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
import com.Mindhubcohort55.Homebanking.services.TransactionServices;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServicesImpl implements TransactionServices {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
private ClientRepository clientRepository;
    @Override
    @Transactional
    public ResponseEntity<?> makeTransaction(MakeTransactionDTO makeTransactionDTO, Authentication authentication) {
        try {
            Client client = clientRepository.findByEmail(authentication.getName());  // Usamos la clase utilitaria
            Account sourceAccount = accountRepository.findByNumber(makeTransactionDTO.sourceAccount());
            Account destinationAccount = accountRepository.findByNumber(makeTransactionDTO.destinationAccount());

            // Validaciones
            if(makeTransactionDTO.sourceAccount().isBlank()){
                return new ResponseEntity<>("The source account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if(makeTransactionDTO.destinationAccount().isBlank()){
                return new ResponseEntity<>("The destination account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if(makeTransactionDTO.amount() == null || makeTransactionDTO.amount().isNaN() || makeTransactionDTO.amount() < 0){
                return new ResponseEntity<>("Enter a valid amount", HttpStatus.FORBIDDEN);
            }

            if(makeTransactionDTO.description().isBlank()){
                return new ResponseEntity<>("The description field must not be empty", HttpStatus.FORBIDDEN);
            }

            if(makeTransactionDTO.sourceAccount().equals(makeTransactionDTO.destinationAccount())){
                return new ResponseEntity<>("The source account and the destination account must not be the same", HttpStatus.FORBIDDEN);
            }

            if(!accountRepository.existsByAccountNumber(makeTransactionDTO.sourceAccount())){
                return new ResponseEntity<>("The source account entered does not exist", HttpStatus.FORBIDDEN);
            }

            if(!accountRepository.existsByIdAndOwner(sourceAccount.getId(), client)){
                return new ResponseEntity<>("The source account entered does not belong to the client", HttpStatus.FORBIDDEN);
            }

            if(!accountRepository.existsByAccountNumber(makeTransactionDTO.destinationAccount())){
                return new ResponseEntity<>("The destination account entered does not exist", HttpStatus.FORBIDDEN);
            }

            if(sourceAccount.getBalance() < makeTransactionDTO.amount()){
                return new ResponseEntity<>("You do not have sufficient balance to carry out the operation", HttpStatus.FORBIDDEN);
            }

            // Crear las transacciones de débito y crédito
            Transaction sourceTransaction = new Transaction(
                    -makeTransactionDTO.amount(),
                    makeTransactionDTO.description() + makeTransactionDTO.sourceAccount(),
                    LocalDateTime.now(),
                    TransactionType.DEBIT
            );
            sourceAccount.addTransaction(sourceTransaction);
            transactionRepository.save(sourceTransaction);

            Transaction destinationTransaction = new Transaction(
                    makeTransactionDTO.amount(),
                    makeTransactionDTO.description() + makeTransactionDTO.destinationAccount(),
                    LocalDateTime.now(),
                    TransactionType.CREDIT
            );
            destinationAccount.addTransaction(destinationTransaction);
            transactionRepository.save(destinationTransaction);

            // Actualizar saldos
            sourceAccount.setBalance(sourceAccount.getBalance() - makeTransactionDTO.amount());
            accountRepository.save(sourceAccount);

            destinationAccount.setBalance(destinationAccount.getBalance() + makeTransactionDTO.amount());
            accountRepository.save(destinationAccount);

            return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
