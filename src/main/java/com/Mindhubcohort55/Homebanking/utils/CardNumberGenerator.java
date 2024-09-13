package com.Mindhubcohort55.Homebanking.utils;

import org.springframework.stereotype.Component;

@Component
public class CardNumberGenerator {

    // Método no estático para generar el número de tarjeta
    public String generateCardNumber() {
        StringBuilder cardNumberBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            String randomNumbers = String.format("%04d", (int) (Math.random() * 9999));
            cardNumberBuilder.append(randomNumbers);
            if (i < 3) {
                cardNumberBuilder.append("-");
            }
        }

        return cardNumberBuilder.toString();
    }
}