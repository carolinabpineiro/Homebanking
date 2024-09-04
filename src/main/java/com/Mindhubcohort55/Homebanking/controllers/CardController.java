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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/current/cards")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDto createCardDto, Authentication authentication){

        try{
            Client client = clientRepository.findByEmail(authentication.getName());

            Set<Card> cardsDebit = client.getCards().stream().filter(card -> card.getCardType() == CardType.DEBIT).collect(Collectors.toSet());
            Set<Card> cardsCredit = client.getCards().stream().filter(card -> card.getCardType() == CardType.CREDIT).collect(Collectors.toSet());


            if(cardsDebit.size() == 3 && createCardDto.cardType() == CardType.DEBIT){
                return new ResponseEntity<>("You have reached the maximum number of allowed Debit cards (3)", HttpStatus.FORBIDDEN);
            }

            if(cardsCredit.size() == 3 && createCardDto.cardType() == CardType.CREDIT){
                return new ResponseEntity<>("You have reached the maximum number of allowed Credit cards (3)", HttpStatus.FORBIDDEN);
            }

            for(Card card : cardsDebit){
                if(card.getCardColor() == createCardDto.cardMembership() && createCardDto.cardType() == CardType.DEBIT){
                    return new ResponseEntity<>("You already have a " + createCardDto.cardMembership() + " debit card", HttpStatus.FORBIDDEN);
                }
            }

            for(Card card : cardsCredit){
                if(card.getCardColor() == createCardDto.cardMembership() && createCardDto.cardType() == CardType.CREDIT){
                    return new ResponseEntity<>("You already have a " + createCardDto.cardMembership() + " credit card", HttpStatus.FORBIDDEN);
                }
            }

            Card newCard = new Card(createCardDto.cardType(), createCardDto.cardMembership(), CardNumberGenerator.makeCardNumber(),CvvGenerator.cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), client);
            client.addCard(newCard);

            cardRepository.save(newCard);
            clientRepository.save(client);

            return new ResponseEntity<>("Card created", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error creating card: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current/cards")
    public ResponseEntity<?> getClientCards(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Card> clientCards = client.getCards();
        Set<CardDto> cardDto = clientCards.stream().map(CardDto::new).collect(Collectors.toSet());

        return new ResponseEntity<>(cardDto, HttpStatus.OK);
    }
}
