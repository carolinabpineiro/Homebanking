package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;
import java.time.LocalDate;

public class CardDto {
    private final Long id;
    private final CardType cardType;
    private final CardColor cardColor;
    private final String cardNumber;
    private final String cvv;
    private final LocalDate fromDate;
    private final LocalDate thruDate;
    private final String cardHolder;

    public CardDto(Card card) {
        this.id = card.getId();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.cardNumber = card.getCardNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cardHolder = card.getCardHolder();
    }

    // Getters
    public Long getId() { return id; }
    public CardType getCardType() { return cardType; }
    public CardColor getCardColor() { return cardColor; }
    public String getCardNumber() { return cardNumber; }
    public String getCvv() { return cvv; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getThruDate() { return thruDate; }
    public String getCardHolder() { return cardHolder; }
}