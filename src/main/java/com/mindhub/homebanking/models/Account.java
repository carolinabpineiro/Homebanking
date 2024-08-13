package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private double balance;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER) //Hay dos tipos de Fetch: LAZY y EAGER (Le estamos diciendo que traiga todo lo que en esta relacion le estamos definiendo.
    @JoinColumn(name = "client_id") //Para ponerle el nombre que yo quiero a esa columna.
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER) //Todas las relaciones son de los dos lados. Va a buscar el nombre de esta propiedad en el muchos en el que lo quiera asociar.
    private Set<Transaction> transactions = new HashSet<>();


    public Account() {
    }

    public Account(String number, double balance, LocalDateTime creationDate) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;

    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }



    public Set<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                ", transactions=" + transactions +
                '}';
    }
}

