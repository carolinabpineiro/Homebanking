package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;

import java.util.List;

public interface AccountService {
    Account getAccountById(long id);

    void saveAcc(Account account);

    List<Account> getAccounts();

    Account getAccByNumber(String number);

    List<Account> getAccByStatus(boolean status);

    List<AccountDto> getAccountsDTO();

    void saveAccount(Account account);

    AccountDto getAccountDTOById(Long id);

    Account getAccountByNumber(String number);

    List<Account> getAccByOwner(Client client);
}