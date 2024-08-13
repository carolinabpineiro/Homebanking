package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;
    private double balance;
    private LocalDate creationDate;
    private String number;
    private Set<TransactionDTO> transactions= new HashSet<>();

    public AccountDTO() {}

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.number = account.getNumber();
        this.transactions = account.getTransactions()
                .stream()
               .map(TransactionDTO::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getNumber() {
        return number;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}

