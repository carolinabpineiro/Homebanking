package com.Mindhubcohort55.Homebanking.models;

import com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator;
import com.Mindhubcohort55.Homebanking.utils.CvvGenerator;
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID único de la tarjeta, generado automáticamente

    @Enumerated(EnumType.STRING)
    private CardType cardType; // Tipo de tarjeta (por ejemplo, débito, crédito)

    @Enumerated(EnumType.STRING)
    private CardColor cardColor; // Color de la tarjeta (por ejemplo, azul, rojo)

    private String cardNumber; // Número de tarjeta, debe ser único

    private String cvv; // Código de verificación de la tarjeta

    private LocalDate fromDate; // Fecha desde la cual la tarjeta es válida

    private LocalDate thruDate; // Fecha hasta la cual la tarjeta es válida

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client; // Cliente asociado con la tarjeta

    private String cardHolder; // Nombre del titular de la tarjeta

    // Constructor vacío
    public Card() {
    }

    // Constructor con parámetros para inicializar todos los atributos
    public Card(CardType cardType, CardColor cardColor, String cardNumber, String cvv, LocalDate fromDate, LocalDate thruDate, String cardHolder, Client client) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.cardHolder = cardHolder;
        this.client = client;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardType=" + cardType +
                ", cardColor=" + cardColor +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", fromDate=" + fromDate +
                ", thruDate=" + thruDate +
                ", client=" + client +
                ", cardHolder='" + cardHolder + '\'' +
                '}';
    }
}