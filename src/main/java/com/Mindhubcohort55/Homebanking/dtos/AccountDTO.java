package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
   private Long id;
    private String number;
    private double balance;
    private LocalDate creationDate;
    private Set<TransactionDTO> transactions= new HashSet<>();

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    public AccountDTO() {
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
    public Set<TransactionDTO> getTransactions() {
        return transactions;

    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}