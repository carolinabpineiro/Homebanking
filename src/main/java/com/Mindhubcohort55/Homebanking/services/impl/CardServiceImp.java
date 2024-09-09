package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import com.Mindhubcohort55.Homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImp implements CardService {

    // Repositorio para gestionar operaciones sobre tarjetas
    @Autowired
    private CardRepository cardRepository;

    /**
     * Obtiene todas las tarjetas en formato DTO.
     *
     * @return Un conjunto de DTOs de tarjetas.
     */
    @Override
    public Set<CardDto> getCardsDTO() {
        // Obtener todas las tarjetas, mapearlas a DTOs y recogerlas en un conjunto
        return cardRepository.findAll().stream().map(CardDto::new).collect(Collectors.toSet());
    }

    /**
     * Guarda una tarjeta en la base de datos después de verificar la cantidad permitida para el cliente.
     *
     * @param card La tarjeta a guardar.
     * @throws RuntimeException Si el cliente ya tiene 3 tarjetas del mismo tipo.
     */
    @Override
    public void saveCard(Card card) {
        // Obtener el cliente asociado con la tarjeta
        Client client = card.getClient();

        // Contar cuántas tarjetas de este tipo ya tiene el cliente
        long cardCount = cardRepository.countByClientAndCardType(client, card.getCardType());
        if (cardCount >= 3) {
            throw new RuntimeException("Client cannot have more than 3 " + card.getCardType() + " cards");
        }

        // Guardar la tarjeta si el cliente tiene menos de 3 tarjetas del mismo tipo
        cardRepository.save(card);
    }

    /**
     * Obtiene una tarjeta por su ID.
     *
     * @param id ID de la tarjeta.
     * @return La tarjeta encontrada, o null si no existe.
     */
    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }
}