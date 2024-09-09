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
        // Buscar el cliente por ID
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Verificar si el cliente ya tiene 3 cuentas
        List<Account> accounts = accountRepository.findByOwner(client);
        if (accounts.size() >= 3) {
            throw new RuntimeException("Client cannot have more than 3 accounts");
        }

        // Generar un número de cuenta único usando el generador
        String accountNumber = AccountNumberGenerator.makeAccountNumber();

        // Crear la cuenta por defecto
        Account defaultAccount = new Account(
                accountNumber,                // Número de cuenta generado
                LocalDateTime.now(),          // Fecha de creación
                0.0,                          // Balance inicial
                true                          // Estado de la cuenta (activa)
        );

        // Asignar la cuenta al cliente
        defaultAccount.setClient(client);

        // Guardar la cuenta en la base de datos
        return accountRepository.save(defaultAccount);
    }

    @Override
    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAcc(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public List<Account> getAccByStatus(boolean status) {
        return accountRepository.findByStatus(status);
    }

    @Override
    public List<AccountDto> getAccountsDTO() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountDto::new).collect(Collectors.toList());
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public AccountDto getAccountDTOById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            return new AccountDto(account);
        }
        return null;
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public List<Account> getAccByOwner(Client client) {
        return accountRepository.findByClient(client); // Buscar todas las cuentas del cliente
    }
}