package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.AccountDTO;
import com.Mindhubcohort55.Homebanking.dtos.TransactionDTO;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountServices {
    List<
            AccountDTO> getAllTransactions();
    AccountDTO getAccountById(Long id);
    List<AccountDTO> getAccountsByClient(Long clientId);
    List<TransactionDTO> getTransactionsByAccountId(Long accountId);
    String createAccount(Authentication authentication);
    List<AccountDTO> getClientAccounts(Authentication authentication);
    Client getClientByEmail (Authentication authentication);
    void addNewClientAccount ();
}
