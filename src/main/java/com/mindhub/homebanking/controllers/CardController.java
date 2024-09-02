package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients/current/cards")
@CrossOrigin(origins = "http://localhost:5173")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<?> getClientCards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Suponiendo que el email es el nombre de usuario

        Client targetClient = clientRepository.findByEmail(email);
        if (targetClient == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        List<CardDTO> cards = cardRepository.findByClientId(targetClient.getId())
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCard(@RequestParam CardType type, @RequestParam CardColor color) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Suponiendo que el email es el nombre de usuario

        Client targetClient = clientRepository.findByEmail(email);
        if (targetClient == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        // Verificar el límite de tarjetas
        long cardCount = cardRepository.countByClientIdAndType(targetClient.getId(), type);
        if (cardCount >= 3) {
            return new ResponseEntity<>("Client already has 3 cards of this type", HttpStatus.FORBIDDEN);
        }

        Card card = new Card();
        card.setNumber(generateRandomCardNumber());
        card.setCvv(generateRandomCvv());
        card.setCardHolder(targetClient.getFirstName() + " " + targetClient.getLastName());
        card.setFromDate(LocalDate.now());
        card.setThruDate(LocalDate.now().plusYears(5));
        card.setType(type);
        card.setColor(color);
        card.setClient(targetClient);

        Card savedCard = cardRepository.save(card);

        return new ResponseEntity<>(new CardDTO(savedCard), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
        }

        cardRepository.deleteById(cardId);
        return new ResponseEntity<>("Card deleted successfully", HttpStatus.OK);
    }

    private String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) cardNumber.append("-");
            cardNumber.append(String.format("%04d", random.nextInt(10000)));
        }
        return cardNumber.toString();
    }

    private int generateRandomCvv() {
        Random random = new Random();
        return random.nextInt(900) + 100; // Generar un número CVV de 3 dígitos
    }
}