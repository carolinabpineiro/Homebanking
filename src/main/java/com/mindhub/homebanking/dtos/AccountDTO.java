package com.mindhub.homebanking.dtos;


import java.time.LocalDate;

public class AccountDTO<Account> {

    private Long id;
    private Double balance;
    private LocalDate creationDate;
    private String number;

    public AccountDTO (Account account){
        this.id = account.getId();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.number = account.getNumber();
    }

    public Long getId() {
        return id;
    }

    public Double getBalance() {
        return balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getNumber() {
        return number;
    }
}

