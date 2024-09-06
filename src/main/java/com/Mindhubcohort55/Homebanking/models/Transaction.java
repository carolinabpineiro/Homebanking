package com.Mindhubcohort55.Homebanking.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Usualmente Long en lugar de long

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // Correcto

    private double amount;
    private String description;
    private LocalDateTime dateTransaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    // Constructor vacío
    public Transaction() {
    }

    // Constructor completo
    public Transaction(TransactionType transactionType, double amount, String description, LocalDateTime dateTransaction, Account account) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.dateTransaction = dateTransaction;
        this.account = account;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", dateTransaction=" + dateTransaction +
                '}';
    }
}