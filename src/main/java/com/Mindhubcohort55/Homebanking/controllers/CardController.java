package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.dtos.CreateCardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.CardService;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator.getRandomCardNumber;
import static com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator.getRandomCvvNumber;

@RestController
@RequestMapping("/api/clients/current/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<Void> createCard(@RequestBody CreateCardDto createCardDto, Authentication authentication) {
        // Obtener el cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Verificar que el cliente no tenga ya 3 tarjetas del mismo tipo
        long cardCount = cardService.countByClientAndCardType(client, createCardDto.type());
        if (cardCount >= 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Crear una nueva tarjeta
        Card card = new Card(
                createCardDto.type(),
                createCardDto.color(),
                CardNumberGenerator.getRandomCardNumber(),
                String.valueOf(CardNumberGenerator.getRandomCvvNumber()),
                LocalDate.now(),
                LocalDate.now().plusYears(5),
                client.getFirstName() + " " + client.getLastName(),
                client
        );

        // Verificar el número de tarjetas y CVV antes de guardar
        if (cardService.existsByCardNumber(card.getCardNumber()) || cardService.existsByCvv(card.getCvv())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Guardar la tarjeta
        cardService.saveCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Set<CardDto>> getCards(Authentication authentication) {
        // Obtener el cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Obtener tarjetas del cliente y convertirlas a DTOs
        Set<CardDto> cards = cardService.getCardsByClient(client);
        return ResponseEntity.ok(cards);
    }
}