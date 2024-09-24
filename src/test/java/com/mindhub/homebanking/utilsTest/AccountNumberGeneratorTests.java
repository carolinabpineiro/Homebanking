package com.mindhub.homebanking.utilsTest;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.utils.AccountNumberGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AccountNumberGeneratorTests {

    @MockBean // Mock para simular el repositorio
    private AccountRepository accountRepository;


    @Test
    public void accountNumberIsCreated() {
        // Crear un repositorio de cuentas simulado
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        // Configurar el repositorio simulado para que siempre devuelva false en existsByNumber
        Mockito.when(accountRepository.existsByNumber(Mockito.anyString())).thenReturn(false);

        // Generar un número de cuenta
        String accountNumber = AccountNumberGenerator.makeAccountNumber(accountRepository);

        // Verificar que el número de cuenta no esté vacío ni sea nulo
        assertThat(accountNumber, is(not(emptyOrNullString())));
        // Verificar que el número de cuenta tenga el formato "VINXXXXXXXX"
        assertThat(accountNumber, matchesPattern("VIN\\d{8}"));
    }
}