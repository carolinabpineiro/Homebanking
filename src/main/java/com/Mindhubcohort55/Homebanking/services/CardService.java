package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;

import java.util.Set;

public interface CardService {

    // Obtiene todas las tarjetas en formato DTO.
    Set<CardDto> getCardsDTO();

    // Guarda una tarjeta en la base de datos.
    void saveCard(Card card);

    // Obtiene una tarjeta por su ID.
    Card getCardById(Long id);
}
