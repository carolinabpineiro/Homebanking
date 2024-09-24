package com.mindhub.homebanking.utilsTest;


import com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CardNumberGeneratorTests {

    @Test
    public void cardNumberIsCreated() {
        // Generar un número de tarjeta
        String cardNumber = CardNumberGenerator.getRandomCardNumber();
        // Verificar que el número de tarjeta no esté vacío ni sea nulo
        assertThat(cardNumber, is(not(emptyOrNullString())));
        // Verificar que el formato del número de tarjeta sea correcto (XXXX-XXXX-XXXX-XXXX)
        assertThat(cardNumber, matchesPattern("\\d{4}-\\d{4}-\\d{4}-\\d{4}"));
    }

    @Test
    public void cvvNumberIsCreated() {
        // Generar un número CVV
        String cvv = CardNumberGenerator.getRandomCvvNumber();
        // Verificar que el CVV no esté vacío ni sea nulo
        assertThat(cvv, is(not(emptyOrNullString())));
        // Verificar que el CVV tenga 3 dígitos
        assertThat(cvv, matchesPattern("\\d{3}"));
    }
}