package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.CardDto;
import com.Mindhubcohort55.Homebanking.models.Card;
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
        return cardRepository.findAll().stream().map(CardDto::new).collect(Collectors.toSet());
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    // Método opcional para desactivar tarjeta
    /*@Override
    public void deleteCard (long id){
        Card card = cardRepository.findById(id).orElse(null);
        if(card != null){
            card.setActive(false);  // Asumiendo que existe un atributo active
            cardRepository.save(card);
        }
    }*/
}