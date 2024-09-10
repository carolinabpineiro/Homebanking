package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.models.TransactionType;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account createDefaultAccount(Long clientId) {
        // Busca el cliente por su ID
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Verifica si el cliente tiene 3 cuentas
        List<Account> accounts = accountRepository.findByClient(client);
        if (accounts.size() >= 3) {
            throw new RuntimeException("Client cannot have more than 3 accounts");
        }

        // Genera un número de cuenta único
        String accountNumber = AccountNumberGenerator.makeAccountNumber();

        // Crea una nueva cuenta
        Account defaultAccount = new Account(
                accountNumber,
                LocalDateTime.now(),
                0.0,
                true
        );

        // Asigna la cuenta al cliente
        defaultAccount.setClient(client);

        // Guarda la cuenta
        return accountRepository.save(defaultAccount);
    }

    @Override
    public Account getAccountById(long id) {
        // Busca una cuenta por ID
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAcc(Account account) {
        // Guarda la cuenta
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        // Obtiene todas las cuentas
        return accountRepository.findAll();
    }

    @Override
    public Account getAccByNumber(String number) {
        // Busca una cuenta por su número
        return accountRepository.findByNumber(number);
    }

    @Override
    public List<Account> getAccByStatus(boolean status) {
        // Busca cuentas por su estado
        return accountRepository.findByStatus(status);
    }

    @Override
    public List<AccountDto> getAccountsDTO() {
        // Convierte las cuentas a DTOs
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountDto::new).collect(Collectors.toList());
    }

    @Override
    public void saveAccount(Account account) {
        // Guarda la cuenta
        accountRepository.save(account);
    }

    @Override
    public AccountDto getAccountDTOById(Long id) {
        // Obtiene el DTO de una cuenta por su ID
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            return new AccountDto(account);
        }
        return null;
    }

    @Override
    public Account getAccountByNumber(String number) {
        // Busca una cuenta por su número
        return accountRepository.findByNumber(number);
    }

    @Override
    public List<Account> getAccByOwner(Client client) {
        // Implementación pendiente
        return List.of();
    }

    @Override
    public List<Account> getAccByClient(Client client) {
        // Busca cuentas por cliente
        return accountRepository.findByClient(client);
    }

    public ResponseEntity<?> makeTransaction(MakeTransactionDto makeTransactionDto, String email) {
        try {
            // Obtiene el cliente autenticado por su email
            Client client = clientRepository.findByEmail(email);

            if (client == null) {
                return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
            }

            // Obtiene las cuentas de origen y destino por sus números
            Account sourceAccount = accountRepository.findByNumber(makeTransactionDto.sourceAccount());
            Account destinationAccount = accountRepository.findByNumber(makeTransactionDto.destinationAccount());

            // Validaciones
            if (makeTransactionDto.sourceAccount().isBlank()) {
                return new ResponseEntity<>("The source account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.destinationAccount().isBlank()) {
                return new ResponseEntity<>("The destination account field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.amount() == null || makeTransactionDto.amount().isNaN() || makeTransactionDto.amount() < 0) {
                return new ResponseEntity<>("Enter a valid amount", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.description().isBlank()) {
                return new ResponseEntity<>("The description field must not be empty", HttpStatus.FORBIDDEN);
            }

            if (makeTransactionDto.sourceAccount().equals(makeTransactionDto.destinationAccount())) {
                return new ResponseEntity<>("The source account and the destination account must not be the same", HttpStatus.FORBIDDEN);
            }

            if (sourceAccount == null) {
                return new ResponseEntity<>("The source account entered does not exist", HttpStatus.FORBIDDEN);
            }

            if (!accountRepository.existsByIdAndClient(sourceAccount.getId(), client)) {
                return new ResponseEntity<>("The source account entered does not belong to the client", HttpStatus.FORBIDDEN);
            }

            if (destinationAccount == null) {
                return new ResponseEntity<>("The destination account entered does not exist", HttpStatus.FORBIDDEN);
            }

            if (sourceAccount.getBalance() < makeTransactionDto.amount()) {
                return new ResponseEntity<>("You do not have sufficient balance to carry out the operation", HttpStatus.FORBIDDEN);
            }

            // Crear y guardar la transacción de débito
            Transaction sourceTransaction = new Transaction(TransactionType.DEBIT, -makeTransactionDto.amount(), makeTransactionDto.description() + makeTransactionDto.sourceAccount(), LocalDateTime.now(), sourceAccount);
            sourceAccount.addTransaction(sourceTransaction);
            transactionRepository.save(sourceTransaction);

            // Crear y guardar la transacción de crédito
            Transaction destinationTransaction = new Transaction(TransactionType.CREDIT, makeTransactionDto.amount(), makeTransactionDto.description() + makeTransactionDto.destinationAccount(), LocalDateTime.now(), destinationAccount);
            destinationAccount.addTransaction(destinationTransaction);
            transactionRepository.save(destinationTransaction);

            // Actualizar balances de las cuentas
            double sourceCurrentBalance = sourceAccount.getBalance();
            sourceAccount.setBalance(sourceCurrentBalance - makeTransactionDto.amount());
            accountRepository.save(sourceAccount);

            double destinationCurrentBalance = destinationAccount.getBalance();
            destinationAccount.setBalance(destinationCurrentBalance + makeTransactionDto.amount());
            accountRepository.save(destinationAccount);

            return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error making transaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}