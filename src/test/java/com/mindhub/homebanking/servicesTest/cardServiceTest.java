package com.mindhub.homebanking.servicesTest;

import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import com.Mindhubcohort55.Homebanking.services.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class cardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByCardNumber() {
        // Configurar el número de tarjeta a verificar
        String cardNumber = "1234567890123456";

        // Configurar el mock del repositorio para devolver true
        when(cardRepository.existsByCardNumber(cardNumber)).thenReturn(true);

        // Ejecutar el método a probar
        boolean exists = cardService.existsByCardNumber(cardNumber);

        // Verificar el resultado
        assertThat(exists).isTrue();



}


}
