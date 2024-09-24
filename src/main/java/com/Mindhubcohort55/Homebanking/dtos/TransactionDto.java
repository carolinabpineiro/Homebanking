package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.Transaction;
import com.Mindhubcohort55.Homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDto {

    private long id;
    private TransactionType transactionType;
    private double amount;
    private String description;
    private LocalDateTime dateTransaction;

    public TransactionDto(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionType = transaction.getType(); // Usa getType() en lugar de getTransactionType()
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.dateTransaction = transaction.getDate(); // Usa getDate() en lugar de getDateTransaction()
    }

    // Getters y setters
    public Long getId() {
        return id;
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
}