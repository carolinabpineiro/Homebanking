package com.Mindhubcohort55.Homebanking.models;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID único del cliente, generado automáticamente

    private String firstName; // Nombre del cliente
    private String lastName; // Apellido del cliente
    private String email; // Correo electrónico del cliente
    private String password; // Contraseña del cliente

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>(); // Conjunto de cuentas asociadas al cliente

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Card> cards = new HashSet<>(); // Conjunto de tarjetas asociadas al cliente

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClientLoan> clientLoans = new HashSet<>(); // Conjunto de préstamos asociados al cliente

    // Constructor vacío
    public Client() {
    }

    // Constructor con parámetros
    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accounts=" + accounts +
                ", cards=" + cards +
                ", clientLoans=" + clientLoans +
                '}';
    }

    // Métodos para añadir relaciones bidireccionales
    public void addAccount(Account account) {
        this.accounts.add(account); // Añade una cuenta al conjunto de cuentas
        account.setClient(this); // Establece el cliente en la cuenta
    }

    public void addCard(Card card) {
        this.cards.add(card); // Añade una tarjeta al conjunto de tarjetas
        card.setClient(this); // Establece el cliente en la tarjeta
    }

    public void addClientLoan(ClientLoan clientLoan) {
        this.clientLoans.add(clientLoan); // Añade un préstamo al conjunto de préstamos
        clientLoan.setClient(this); // Establece el cliente en el préstamo
    }
}