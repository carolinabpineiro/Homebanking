package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Set<CardDto> getCardsByClient(Client client) {
        return cardRepository.findByClient(client).stream()
                .map(CardDto::new)
                .collect(Collectors.toSet());
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByCardNumber(String cardNumber) {
        return cardRepository.existsByCardNumber(cardNumber);
    }

    @Override
    public boolean existsByCvv(String cvv) {
        return cardRepository.existsByCvv(cvv);
    }

    @Override
    public long countByClientAndCardType(Client client, CardType cardType) {
        return cardRepository.countByClientAndCardType(client, cardType);
    }

    @Override
    public List<CardDto> getAllCardsDTO() {
        return cardRepository.findAll().stream()
                .map(CardDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> createCard(Authentication authentication, CardDto cardDto) {
        Client client = getClient(authentication);

        if (validateCardDto(cardDto) != null) {
            return new ResponseEntity<>(validateCardDto(cardDto), HttpStatus.BAD_REQUEST);
        }

        if (validateDetailsCard(client, cardDto) != null) {
            return new ResponseEntity<>(validateDetailsCard(client, cardDto), HttpStatus.FORBIDDEN);
        }

        if (validateColor(client, cardDto.getCardColor(), cardDto.getCardType()) != null) {
            return new ResponseEntity<>(validateColor(client, cardDto.getCardColor(), cardDto.getCardType()), HttpStatus.FORBIDDEN);
        }

        Card card = generateCard(cardDto);
        return ResponseEntity.ok().body(saveCard(client, card));
    }

    @Override
    public String validateCardDto(CardDto cardDto) {
        if (cardDto.getCardType() == null) {
            return "The 'type' field is required.";
        }

        if (cardDto.getCardColor() == null) {
            return "The 'color' field is required.";
        }

        return null;
    }

    @Override
    public String validateDetailsCard(Client client, CardDto cardDto) {
        if (cardDto.getCardType() == CardType.DEBIT) {
            if (getAllCardsDebits(client).size() >= 3) {
                return "You can't have more than 3 debit cards";
            }
        } else {
            if (getAllCardsCredits(client).size() >= 3) {
                return "You can't have more than 3 credit cards";
            }
        }
        return null;
    }

    @Override
    public String validateColor(Client client, CardColor color, CardType type) {
        if (type == CardType.DEBIT) {
            if (getAllCardsDebits(client).stream().anyMatch(card -> card.getCardColor() == color)) {
                return "You already have a debit card with this color";
            }
        } else {
            if (getAllCardsCredits(client).stream().anyMatch(card -> card.getCardColor() == color)) {
                return "You already have a credit card with this color";
            }
        }

        return null;
    }

    @Override
    public List<Card> getAllCardsCredits(Client client) {
        return client.getCards().stream()
                .filter(card -> card.getCardType() == CardType.CREDIT)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> getAllCardsDebits(Client client) {
        return client.getCards().stream()
                .filter(card -> card.getCardType() == CardType.DEBIT)
                .collect(Collectors.toList());
    }

    @Override
    public Card generateCard(CardDto cardDto) {
        return new Card(cardDto.getCardType(), cardDto.getCardColor(),
                generateCardNumber(), generateCvv(),
                LocalDate.now(), LocalDate.now().plusYears(5),
                cardDto.getCardHolder(), null);
    }

    @Override
    public CardDto saveCard(Client client, Card card) {
        client.addCard(card);
        cardRepository.save(card);
        return new CardDto(card);
    }

    @Override
    public CardColor getCardColor(String color) {
        return CardColor.valueOf(color.toUpperCase());
    }

    @Override
    public CardType getCardType(String type) {
        return CardType.valueOf(type.toUpperCase());
    }

    private String generateCardNumber() {
        // Implementación para generar un número de tarjeta
        return "1234567812345678"; // Ejemplo
    }

    private String generateCvv() {
        // Implementación para generar un CVV
        return "123"; // Ejemplo
    }

    private Client getClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }
}