package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.dtos.CreateCardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.CardRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.CardService;
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
    private CardService cardService; // Inyectar el servicio para delegar la lógica de negocio

    @Transactional
    @PostMapping("/cards")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDto createCardDto, Authentication authentication) {
        try {
            // Obtener el cliente autenticado usando el repositorio
            Client client = clientRepository.findByEmail(authentication.getName());

            if (client == null) {
                return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
            }

            // Crear una nueva tarjeta utilizando el servicio
            Card newCard = new Card(
                    createCardDto.cardType(),
                    createCardDto.cardColor(),
                    createCardDto.cardNumber(),
                    createCardDto.cvv(),
                    createCardDto.expiryDate(), // `expiryDate` como `fromDate`
                    createCardDto.issueDate(),  // `issueDate` como `thruDate`
                    "Card Holder Name", // Asignar el nombre del titular correctamente
                    client
            );

            // Delegar la creación de la tarjeta al servicio
            cardService.saveCard(newCard);

            return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating card: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}