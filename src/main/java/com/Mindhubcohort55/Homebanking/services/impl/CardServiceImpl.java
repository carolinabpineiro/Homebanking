package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import com.Mindhubcohort55.Homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Set<CardDto> getCardsByClient(Client client) {
        // Obtener tarjetas del cliente y convertirlas a DTOs
        return cardRepository.findByClient(client).stream()
                .map(CardDto::new)
                .collect(Collectors.toSet());
    }

    @Override
    public void saveCard(Card card) {
        // Guardar tarjeta
        cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long id) {
        // Obtener tarjeta por ID
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByCardNumber(String cardNumber) {
        // Verificar si existe una tarjeta con el número dado
        return cardRepository.existsByCardNumber(cardNumber);
    }

    @Override
    public boolean existsByCvv(String cvv) {
        // Verificar si existe una tarjeta con el CVV dado
        return cardRepository.existsByCvv(cvv);
    }

    @Override
    public long countByClientAndCardType(Client client, CardType cardType) {
        // Contar tarjetas del cliente por tipo
        return cardRepository.countByClientAndCardType(client, cardType);
    }
}