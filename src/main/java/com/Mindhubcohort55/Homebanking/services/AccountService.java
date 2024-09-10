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

        // Crea una cuenta predeterminada para el cliente especificado.
        Account createDefaultAccount(Long clientId);

        // Obtiene una cuenta por su ID.
        Account getAccountById(long id);

        // Guarda una cuenta en la base de datos.
        void saveAcc(Account account);

        // Obtiene todas las cuentas.
        List<Account> getAccounts();

        // Obtiene una cuenta por su número.
        Account getAccByNumber(String number);

        // Obtiene cuentas por estado (activa/inactiva).
        List<Account> getAccByStatus(boolean status);

        // Obtiene todas las cuentas en formato DTO.
        List<AccountDto> getAccountsDTO();

        // Guarda una cuenta en la base de datos.
        void saveAccount(Account account);

        // Obtiene una cuenta en formato DTO por su ID.
        AccountDto getAccountDTOById(Long id);

        // Obtiene una cuenta por su número.
        Account getAccountByNumber(String number);

        // Obtiene todas las cuentas asociadas a un cliente.
        List<Account> getAccByOwner(Client client);

        // Obtiene todas las cuentas asociadas a un cliente por nombre.
        List<Account> getAccByClient(Client client);
}

