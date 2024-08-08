package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;
    private double amount;
    private String description;
    private String type;
    private LocalDate date;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.type = transaction.getType().toString();
        this.date = transaction.getDate();
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }
}
