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

        /**
         * Crea una cuenta por defecto para un cliente dado.
         *
         * @param clientId ID del cliente para quien se crea la cuenta.
         * @return La cuenta creada.
         */
        Account createDefaultAccount(Long clientId);

        /**
         * Obtiene una cuenta por su ID.
         *
         * @param id ID de la cuenta.
         * @return La cuenta encontrada, o null si no existe.
         */
        Account getAccountById(long id);

        /**
         * Guarda una cuenta en la base de datos.
         *
         * @param account La cuenta a guardar.
         */
        void saveAcc(Account account);

        /**
         * Obtiene todas las cuentas en la base de datos.
         *
         * @return Una lista de todas las cuentas.
         */
        List<Account> getAccounts();

        /**
         * Obtiene una cuenta por su número.
         *
         * @param number Número de la cuenta.
         * @return La cuenta encontrada, o null si no existe.
         */
        Account getAccByNumber(String number);

        /**
         * Obtiene todas las cuentas que tienen un estado específico.
         *
         * @param status Estado de la cuenta (true para activa, false para inactiva).
         * @return Una lista de cuentas con el estado dado.
         */
        List<Account> getAccByStatus(boolean status);

        /**
         * Obtiene todas las cuentas en formato DTO.
         *
         * @return Una lista de DTOs de cuentas.
         */
        List<AccountDto> getAccountsDTO();

        /**
         * Guarda una cuenta en la base de datos.
         *
         * @param account La cuenta a guardar.
         */
        void saveAccount(Account account);

        /**
         * Obtiene una cuenta en formato DTO por su ID.
         *
         * @param id ID de la cuenta.
         * @return El DTO de la cuenta encontrada, o null si no existe.
         */
        AccountDto getAccountDTOById(Long id);

        /**
         * Obtiene una cuenta por su número.
         *
         * @param number Número de la cuenta.
         * @return La cuenta encontrada, o null si no existe.
         */
        Account getAccountByNumber(String number);

        /**
         * Obtiene todas las cuentas de un cliente dado.
         *
         * @param client Cliente cuyo cuentas se desean obtener.
         * @return Una lista de cuentas asociadas al cliente.
         */
        List<Account> getAccByOwner(Client client);

        /**
         * Obtiene todas las cuentas de un cliente dado, usando un nombre de cliente.
         *
         * @param client Cliente cuyo cuentas se desean obtener.
         * @return Una lista de cuentas asociadas al cliente.
         */
        List<Account> getAccByClient(Client client);
}

