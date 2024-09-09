package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {

        Account createDefaultAccount(Long clientId);

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

    List<Account> getAccByClient(Client client);
}


