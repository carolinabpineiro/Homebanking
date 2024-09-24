package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDto {
    private Long id;
    private String number;
    private double balance;
    private LocalDateTime creationDate; // Cambia a LocalDateTime
    private Set<TransactionDto> transactions = new HashSet<>();

    public AccountDto(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate(); // No es necesario convertir
        this.transactions = account.getTransactions().stream().map(TransactionDto::new).collect(Collectors.toSet());
    }

    public AccountDto() {
    }

    // Getters y Setters
    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public Long getId() {
        return id;
    }

    public Set<TransactionDto> getTransactions() {
        return transactions;
    }

    public LocalDateTime getCreationDate() {
        return creationDate; // Cambia a LocalDateTime
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate; // Cambia a LocalDateTime
    }
}