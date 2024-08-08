package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

    @Entity
    public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private TransactionType type;
        private Double amount;
        private String description;
        private LocalDate date;

        @ManyToOne
        @JoinColumn(name = "account_id")
        private Account account;

        public Transaction() {}

        public Transaction(TransactionType type, Double amount, String description, LocalDate date, Account account) {
            this.type = type;
            this.amount = amount;
            this.description = description;
            this.date = date;
            this.account = account;
        }

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public TransactionType getType() {
            return type;
        }

        public void setType(TransactionType type) {
            this.type = type;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }
    }

