package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;

public record CreateCardDTO(CardType cardType, CardColor cardColor) {
}
