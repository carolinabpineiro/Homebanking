package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.dtos.CreateCardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator;
import com.Mindhubcohort55.Homebanking.utils.CvvGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @Transactional
    @PostMapping("/cards")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDto createCardDto, Authentication authentication) {

        try {
            // Obtener el cliente autenticado usando el repositorio
            Client client = clientRepository.findByEmail(authentication.getName());

            if (client == null) {
                return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
            }

            Card newCard = new Card(
                    createCardDto.cardType(),
                    createCardDto.cardColor(),
                    createCardDto.cardNumber(),
                    createCardDto.cvv(),
                    createCardDto.expiryDate(), // Esto es `fromDate`
                    createCardDto.issueDate(),  // Esto es `thruDate`
                    "Card Holder Name", // Asegúrate de agregar el nombre del titular
                    client
            );
            client.addCard(newCard);
            cardRepository.save(newCard);

            return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating card: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}