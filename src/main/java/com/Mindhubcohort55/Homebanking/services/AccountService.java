package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.dtos.MakeTransactionDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    // Crea una cuenta predeterminada para un nuevo cliente.
    Account createDefaultAccountForNewClient();

    // Crea una cuenta predeterminada para un cliente existente.
    Account createDefaultAccount(Long clientId);

    // Obtiene una cuenta por su ID.
    Optional<Account> getAccountById(long id);

    // Guarda una cuenta en la base de datos.
    void saveAccount(Account account);

    // Obtiene todas las cuentas.
    List<Account> getAccounts();

    // Obtiene una cuenta por su número.
    Account getAccountByNumber(String number);

    // Verifica si existe una cuenta con un número específico.
    boolean existsByAccountNumber(String accountNumber);

    // Obtiene cuentas por estado (activo/inactivo).
    List<Account> getAccountsByStatus(boolean status);

    // Obtiene todas las cuentas en formato DTO.
    List<AccountDto> getAccountsDTO();

    // Obtiene un DTO de cuenta por su ID.
    AccountDto getAccountDTOById(Long id);

    // Obtiene cuentas asociadas a un cliente.
    List<Account> getAccountsByClient(Client client);

    // Realiza una transacción.
    ResponseEntity<?> makeTransaction(MakeTransactionDto makeTransactionDto, String email);

    // Elimina una cuenta de la base de datos.
    void deleteAccount(Long accountId);

    // Actualiza el balance de una cuenta.
    void updateAccountBalance(Long accountId, double newBalance);

    // Actualiza una cuenta en la base de datos.
    void updateAccount(Account account);
}