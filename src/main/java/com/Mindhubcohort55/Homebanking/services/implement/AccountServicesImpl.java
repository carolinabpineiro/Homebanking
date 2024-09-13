package com.Mindhubcohort55.Homebanking.services.implement;

import com.Mindhubcohort55.Homebanking.dtos.AccountDTO;
import com.Mindhubcohort55.Homebanking.dtos.TransactionDTO;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.TransactionRepository;
import com.Mindhubcohort55.Homebanking.services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServicesImpl implements AccountServices {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAllTransactions() {
        // Implementación aquí
        return null; // Actualiza con la lógica real
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        // Implementación aquí
        return null; // Actualiza con la lógica real
    }

    @Override
    public List<AccountDTO> getAccountsByClient(Long clientId) {
        // Implementación aquí
        return null; // Actualiza con la lógica real
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        // Implementación aquí
        return null; // Actualiza con la lógica real
    }

    @Override
    public String createAccount(Authentication authentication) {
        // Implementación aquí
        return null; // Actualiza con la lógica real
    }

    @Override
    public List<AccountDTO> getClientAccounts(Authentication authentication) {
        // Implementación aquí
        return null; // Actualiza con la lógica real
    }

    @Override
    public Client getClientByEmail(Authentication authentication) {
        // Implementación aquí
        return null; // Actualiza con la lógica real
    }

    @Override
    public void addNewClientAccount() {
        // Implementación del método addNewClientAccount

        // Ejemplo básico de implementación
        Client client = clientRepository.findByEmail("example@example.com"); // Obtén un cliente específico

        if (client != null && client.getAccounts().size() < 3) {
            String accountNumber = generateUniqueAccountNumber();
            Account newAccount = new Account(accountNumber, LocalDate.now(), 0.0);
            client.addAccount(newAccount);
            accountRepository.save(newAccount);
            clientRepository.save(client);
        } else {
            throw new RuntimeException("El cliente ya tiene el número máximo de cuentas o no existe.");
        }
    }

    private String generateUniqueAccountNumber() {
        String leftZero;
        do {
            leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
        } while (accountRepository.existsByAccountNumber("VIN" + leftZero));
        return "VIN" + leftZero;
    }
}