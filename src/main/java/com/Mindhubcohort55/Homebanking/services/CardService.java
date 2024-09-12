package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;

import java.util.Set;

public interface CardService {
    Set<CardDto> getCardsByClient(Client client);
    void saveCard(Card card);
    Card getCardById(Long id);
    boolean existsByCardNumber(String cardNumber);
    boolean existsByCvv(String cvv);
    long countByClientAndCardType(Client client, CardType cardType);
}