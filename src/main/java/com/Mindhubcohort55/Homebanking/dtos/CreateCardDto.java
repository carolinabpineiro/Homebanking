package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;

import java.time.LocalDate;

public record CreateCardDto(
        CardType cardType,
        CardColor cardColor,
        String cardNumber,
        String cvv,
        LocalDate expiryDate,
        LocalDate issueDate,
        String cardHolder // Agregado aquí
) {
}