package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany (mappedBy ="owner", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    // Constructor vacío para JPA
    public Client() {
    }

    // Constructor con parámetros
    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Constructor alternativo
    public Client(String firstName, String lastName) {
        this(firstName, lastName, ""); // Llama al constructor con parámetros
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void SetAccounts(Set<Account> accounts) {this.accounts = accounts;}

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                '}';
        }
    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
}