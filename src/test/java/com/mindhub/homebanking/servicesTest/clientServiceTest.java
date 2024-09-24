package com.mindhub.homebanking.servicesTest;

import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class clientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClientByEmail() {
        // Crear un objeto Client para la prueba
        Client client = new Client();
        client.setId(1L);
        client.setEmail("test@example.com");
        // Establecer más propiedades si es necesario

        // Configurar el comportamiento del mock para el repositorio
        when(clientRepository.findByEmail("test@example.com")).thenReturn(client);

        // Ejecutar el método a probar
        Client result = clientService.getClientByEmail("test@example.com");

        // Verificar el resultado
        assertEquals(client, result);
    }
}

