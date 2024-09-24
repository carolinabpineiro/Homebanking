package com.mindhub.homebanking.servicesTest;

import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;

import com.Mindhubcohort55.Homebanking.services.ClientService;
import com.Mindhubcohort55.Homebanking.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private ClientService clientService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        // Simular la autenticación
        when(authentication.getName()).thenReturn("test@example.com");

        // Simular el cliente encontrado
        Client client = new Client();
        client.setEmail("test@example.com");
        when(clientRepository.findByEmail("test@example.com")).thenReturn(client);

        // Mockear el método del clientService
        when(clientService.getClientByEmail("test@example.com")).thenReturn(client);

        // Ejecutar el método a probar
        ResponseEntity<String> response = accountService.createAccount(authentication);

        // Verificar el resultado
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Account created successfully");  // Cambia aquí el mensaje esperado

        // Verificar que se guardó la cuenta
        verify(accountRepository, times(1)).save(any(Account.class));
    }

}