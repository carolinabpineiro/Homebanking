package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
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
}