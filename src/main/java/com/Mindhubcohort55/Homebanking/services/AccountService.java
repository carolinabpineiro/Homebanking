package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Account createDefaultAccount(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Crear la cuenta por defecto con los parámetros requeridos por el constructor existente
        Account defaultAccount = new Account(
                "123456789",                   // Número de cuenta
                LocalDateTime.now(),           // Fecha de creación
                0.0,                           // Balance inicial
                true                           // Estado de la cuenta (activa)
        );

        // Aquí es donde se guarda la cuenta
        return accountRepository.save(defaultAccount);
    }

    public abstract Account getAccountById(long id);

    public abstract void saveAcc(Account account);

    public abstract List<Account> getAccounts();

    public abstract Account getAccByNumber(String number);

    public abstract List<Account> getAccByStatus(boolean status);

    public abstract List<AccountDto> getAccountsDTO();

    public abstract void saveAccount(Account account);

    public abstract AccountDto getAccountDTOById(Long id);

    public abstract Account getAccountByNumber(String number);

    public abstract List<Account> getAccByOwner(Client client);
}