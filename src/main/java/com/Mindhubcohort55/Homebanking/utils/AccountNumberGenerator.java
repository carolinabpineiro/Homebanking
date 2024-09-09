package com.Mindhubcohort55.Homebanking.utils;

import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountNumberGenerator {

    @Autowired
    public static AccountRepository accountRepository; // Inyección de dependencias para el repositorio de cuentas.

    // Constructor para la inyección de dependencias.
    public AccountNumberGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Método para generar un número de cuenta único.
    public static String makeAccountNumber() {

        String leftZero;

        // Generar un número de cuenta único hasta que se encuentre uno que no exista en el repositorio.
        do {
            // Genera un número aleatorio de 8 dígitos.
            leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
            // Alternativa comentada usando Random (esto es opcional).
            // leftZero = String.format("%08d", random.nextInt(100000000));
        } while (accountRepository.existsByNumber("VIN" + leftZero)); // Verifica que el número generado no exista ya.

        // Devuelve el número de cuenta en formato "VINXXXXXXXX", donde "XXXXXXXX" es el número generado.
        return "VIN" + leftZero;
    }
}