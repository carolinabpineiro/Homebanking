package com.Mindhubcohort55.Homebanking.utils;

import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

public class AccountNumberGenerator {

    // Genera un número de cuenta único.
    public static String makeAccountNumber(AccountRepository accountRepository) {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (!isAccountNumberUnique(accountNumber, accountRepository));
        return accountNumber;
    }

    // Genera un número aleatorio de cuenta con formato "VINXXXXXXXX".
    private static String generateAccountNumber() {
        // Genera un número aleatorio de 8 dígitos.
        String leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
        // Retorna el número de cuenta con el prefijo "VIN".
        return "VIN" + leftZero;
    }

    // Verifica si el número de cuenta es único.
    private static boolean isAccountNumberUnique(String accountNumber, AccountRepository accountRepository) {
        // Verifica que el número generado no exista ya en el repositorio.
        return !accountRepository.existsByNumber(accountNumber);
    }
}