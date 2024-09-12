package com.Mindhubcohort55.Homebanking.utils;

import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CardNumberGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String getRandomCardNumber() {
        return String.format("%04d-%04d-%04d-%04d",
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000),
                RANDOM.nextInt(10000));
    }

    public static String getRandomCvvNumber() {
        return String.format("%03d", 100 + RANDOM.nextInt(900)); // Genera un número CVV entre 100 y 999
    }
}