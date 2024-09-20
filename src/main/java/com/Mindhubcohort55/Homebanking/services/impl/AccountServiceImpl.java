package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import com.Mindhubcohort55.Homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    // Crear una cuenta por defecto para un cliente nuevo
    @Override
    public Account createDefaultAccountForNewClient() {
        String accountNumber = AccountNumberGenerator.makeAccountNumber(accountRepository);
        Account defaultAccount = new Account(accountNumber, LocalDateTime.now(), 0.0, true);
        return accountRepository.save(defaultAccount);
    }

    // Crear una cuenta para un cliente específico
    @Override
    public Account createDefaultAccount(Long clientId) {
        Client client = clientService.findClientById(clientId);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        // Validar que el cliente no tenga más de 3 cuentas
        List<Account> accounts = accountRepository.findByClient(client);
        if (accounts.size() >= 3) {
            throw new RuntimeException("Client cannot have more than 3 accounts");
        }

        String accountNumber = AccountNumberGenerator.makeAccountNumber(accountRepository);
        Account defaultAccount = new Account(accountNumber, LocalDateTime.now(), 0.0, true);
        defaultAccount.setClient(client);
        return accountRepository.save(defaultAccount);
    }

    // Crear cuenta a través de autenticación
    @Override
    public ResponseEntity<String> createAccount(Authentication authentication) {
        Client client = clientService.getClientByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        // Validar límite de cuentas (máximo 3)
        List<Account> accounts = accountRepository.findByClient(client);
        if (accounts.size() >= 3) {
            return new ResponseEntity<>("You cannot have more than 3 accounts", HttpStatus.FORBIDDEN);
        }

        String accountNumber = AccountNumberGenerator.makeAccountNumber(accountRepository);
        Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0.0, true);
        newAccount.setClient(client);
        accountRepository.save(newAccount);

        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }

    // Obtener cuenta por su ID
    @Override
    public Optional<Account> getAccountById(long id) {
        return accountRepository.findById(id);
    }

    // Guardar una cuenta
    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    // Obtener DTO de cuenta por ID
    @Override
    public ResponseEntity<AccountDto> getAccountDTO(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (!accountOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AccountDto accountDto = new AccountDto(accountOptional.get());
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    // Eliminar una cuenta
    @Override
    public ResponseEntity<String> deleteAccount(Long id, Authentication authentication) {
        Client client = clientService.getClientByEmail(authentication.getName());
        Optional<Account> accountOptional = accountRepository.findById(id);

        if (!accountOptional.isPresent() || !accountOptional.get().getClient().equals(client)) {
            return new ResponseEntity<>("Account not found or not owned by the client", HttpStatus.FORBIDDEN);
        }

        accountRepository.delete(accountOptional.get());
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
    }

    // Obtener todas las cuentas
    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    // Obtener cuenta por número
    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    // Verificar si existe una cuenta con el número dado
    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByNumber(accountNumber);
    }

    // Obtener cuentas por estado (activa/inactiva)
    @Override
    public List<Account> getAccountsByStatus(boolean status) {
        return accountRepository.findByStatus(status);
    }

    // Obtener todas las cuentas en formato DTO
    @Override
    public List<AccountDto> getAccountsDTO() {
        return accountRepository.findAll().stream()
                .map(AccountDto::new)
                .collect(Collectors.toList());
    }

    // Obtener DTO de cuenta por ID
    @Override
    public AccountDto getAccountDTOById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return new AccountDto(account);
    }

    // Obtener cuentas por cliente
    @Override
    public List<Account> getAccountsByClient(Client client) {
        return accountRepository.findByClient(client);
    }

    // Realizar transacción
    @Override
    public ResponseEntity<?> makeTransaction(MakeTransactionDto makeTransactionDto, String email) {
        // Implementar lógica de transacción
        // ...
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Eliminar cuenta por ID
    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    // Actualizar el balance de una cuenta
    @Override
    public void updateAccountBalance(Long accountId, double newBalance) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    // Actualizar una cuenta
    @Override
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }
}