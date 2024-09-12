package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import com.Mindhubcohort55.Homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private  AccountRepository accountRepository;

    @Autowired
    private  ClientService clientService;



    @Override
    public Account createDefaultAccountForNewClient() {
        String accountNumber = AccountNumberGenerator.makeAccountNumber(accountRepository);
        Account defaultAccount = new Account(accountNumber, LocalDateTime.now(), 0.0, true);
        return accountRepository.save(defaultAccount);
    }

    @Override
    public Account createDefaultAccount(Long clientId) {
        // Usamos el servicio en lugar del repositorio, y validamos si el cliente es null
        Client client = clientService.findClientById(clientId);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        List<Account> accounts = accountRepository.findByClient(client);
        if (accounts.size() >= 3) {
            throw new RuntimeException("Client cannot have more than 3 accounts");
        }

        String accountNumber = AccountNumberGenerator.makeAccountNumber(accountRepository);
        Account defaultAccount = new Account(accountNumber, LocalDateTime.now(), 0.0, true);
        defaultAccount.setClient(client);
        return accountRepository.save(defaultAccount);
    }


    @Override
    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public List<Account> getAccountsByStatus(boolean status) {
        return accountRepository.findByStatus(status);
    }

    @Override
    public List<AccountDto> getAccountsDTO() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountDto::new).collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountDTOById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        return account != null ? new AccountDto(account) : null;
    }

    @Override
    public List<Account> getAccountsByClient(Client client) {
        return accountRepository.findByClient(client);
    }

    @Override
    public ResponseEntity<?> makeTransaction(MakeTransactionDto makeTransactionDto, String email) {
        // Aquí deberías implementar la lógica para realizar una transacción.
        return null;
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public void updateAccountBalance(Long accountId, double newBalance) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}