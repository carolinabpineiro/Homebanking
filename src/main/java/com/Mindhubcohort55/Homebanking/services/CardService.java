package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;

import java.util.Set;

public interface CardService {

    /**
     * Obtiene todas las tarjetas en formato DTO.
     *
     * @return Un conjunto de DTOs de tarjetas.
     */
    Set<CardDto> getCardsDTO();

    /**
     * Guarda una tarjeta en la base de datos.
     *
     * @param card La tarjeta a guardar.
     */
    void saveCard(Card card);

    /**
     * Obtiene una tarjeta por su ID.
     *
     * @param id ID de la tarjeta.
     * @return La tarjeta encontrada, o null si no existe.
     */
    Card getCardById(Long id);

    // Método opcional para desactivar tarjeta
    // /**
    //  * Desactiva una tarjeta en la base de datos por su ID.
    //  *
    //  * @param id ID de la tarjeta a desactivar.
    //  */
    // void deleteCard(long id);
}

