package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;

import java.util.Set;

public interface CardService {

    Set<CardDto> getCardsDTO();

    void saveCard(Card card);

    Card getCardById(Long id);

    // Método opcional para desactivar tarjeta
    // void deleteCard(long id);
}
