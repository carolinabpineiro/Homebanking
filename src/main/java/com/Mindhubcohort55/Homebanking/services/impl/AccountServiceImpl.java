package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
        return List.of();
    }
}