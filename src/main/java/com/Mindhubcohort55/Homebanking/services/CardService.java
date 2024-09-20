package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface CardService {

    Set<CardDto> getCardsByClient(Client client);

    void saveCard(Card card);

    Card getCardById(Long id);

    boolean existsByCardNumber(String cardNumber);

    boolean existsByCvv(String cvv);

    long countByClientAndCardType(Client client, CardType cardType);

    List<CardDto> getAllCardsDTO();

    ResponseEntity<?> createCard(Authentication authentication, CardDto cardDto); // Cambiado a CardDto

    String validateCardDto(CardDto cardDto); // Cambiado a CardDto

    String validateDetailsCard(Client client, CardDto cardDto); // Cambiado a CardDto

    String validateColor(Client client, CardColor color, CardType type);

    List<Card> getAllCardsCredits(Client client);

    List<Card> getAllCardsDebits(Client client);

    Card generateCard(CardDto cardDto); // Cambiado a CardDto

    CardDto saveCard(Client client, Card card);

    CardColor getCardColor(String color);

    CardType getCardType(String type);
}