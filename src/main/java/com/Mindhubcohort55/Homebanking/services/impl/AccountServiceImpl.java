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

    // Repositorio para gestionar operaciones sobre cuentas
    private final AccountRepository accountRepository;
    // Repositorio para gestionar operaciones sobre clientes
    private final ClientRepository clientRepository;

    // Constructor para inyección de dependencias
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Crea una cuenta por defecto para un cliente dado.
     *
     * @param clientId ID del cliente para el cual se debe crear la cuenta.
     * @return La cuenta creada y guardada en la base de datos.
     * @throws RuntimeException Si el cliente no existe o ya tiene 3 cuentas.
     */
    @Override
    public Account createDefaultAccount(Long clientId) {
        // Buscar el cliente por ID
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Verificar si el cliente ya tiene 3 cuentas
        List<Account> accounts = accountRepository.findByClient(client);
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

    /**
     * Obtiene una cuenta por su ID.
     *
     * @param id ID de la cuenta.
     * @return La cuenta encontrada, o null si no existe.
     */
    @Override
    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    /**
     * Guarda una cuenta en la base de datos.
     *
     * @param account La cuenta a guardar.
     */
    @Override
    public void saveAcc(Account account) {
        accountRepository.save(account);
    }

    /**
     * Obtiene todas las cuentas.
     *
     * @return Lista de todas las cuentas.
     */
    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Obtiene una cuenta por su número.
     *
     * @param number Número de la cuenta.
     * @return La cuenta encontrada, o null si no existe.
     */
    @Override
    public Account getAccByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    /**
     * Obtiene cuentas basadas en su estado (activo/inactivo).
     *
     * @param status Estado de la cuenta.
     * @return Lista de cuentas con el estado especificado.
     */
    @Override
    public List<Account> getAccByStatus(boolean status) {
        return accountRepository.findByStatus(status);
    }

    /**
     * Obtiene todas las cuentas y las convierte a DTOs.
     *
     * @return Lista de DTOs de cuentas.
     */
    @Override
    public List<AccountDto> getAccountsDTO() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountDto::new).collect(Collectors.toList());
    }

    /**
     * Guarda una cuenta en la base de datos.
     *
     * @param account La cuenta a guardar.
     */
    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    /**
     * Obtiene un DTO de cuenta por su ID.
     *
     * @param id ID de la cuenta.
     * @return DTO de la cuenta encontrada, o null si no existe.
     */
    @Override
    public AccountDto getAccountDTOById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            return new AccountDto(account);
        }
        return null;
    }

    /**
     * Obtiene una cuenta por su número.
     *
     * @param number Número de la cuenta.
     * @return La cuenta encontrada, o null si no existe.
     */
    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    /**
     * Obtiene cuentas asociadas a un cliente dado.
     *
     * @param client Cliente para buscar sus cuentas.
     * @return Lista de cuentas del cliente.
     */
    @Override
    public List<Account> getAccByOwner(Client client) {
        return List.of(); // Nota: Esta implementación no hace nada y debe ser revisada.
    }

    /**
     * Obtiene cuentas asociadas a un cliente dado.
     *
     * @param client Cliente para buscar sus cuentas.
     * @return Lista de cuentas del cliente.
     */
    @Override
    public List<Account> getAccByClient(Client client) {
        return accountRepository.findByClient(client); // Buscar todas las cuentas del cliente
    }
}