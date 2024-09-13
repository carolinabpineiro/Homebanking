package com.Mindhubcohort55.Homebanking.utils;

import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CvvGenerator {

    private final CardRepository cardRepository;

    // Inyección de CardRepository en el constructor
    @Autowired
    public CvvGenerator(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public int generateCvv() {
        int cvvNumber;

        do {
            cvvNumber = (int) (Math.random() * 1000);  // Genera un número aleatorio entre 0 y 999
        } while (cardRepository.existsByCvv(String.valueOf(cvvNumber)));  // Verifica si ya existe

        return cvvNumber;  // Retorna el número como entero
    }
}