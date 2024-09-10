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

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Set<CardDto> getCardsDTO() {
        // Obtener todas las tarjetas y convertirlas a DTOs
        return cardRepository.findAll().stream().map(CardDto::new).collect(Collectors.toSet());
    }

    @Override
    public void saveCard(Card card) {
        // Verificar si el cliente tiene más de 3 tarjetas del mismo tipo
        Client client = card.getClient();
        long cardCount = cardRepository.countByClientAndCardType(client, card.getCardType());
        if (cardCount >= 3) {
            throw new RuntimeException("Client cannot have more than 3 " + card.getCardType() + " cards");
        }

        // Guardar la tarjeta
        cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long id) {
        // Buscar una tarjeta por su ID
        return cardRepository.findById(id).orElse(null);
    }
}