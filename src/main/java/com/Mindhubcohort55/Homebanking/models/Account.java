package com.Mindhubcohort55.Homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

    @Entity
    public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String number;
        private LocalDateTime creationDate;
        private double balance;
        private boolean status;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "client_id")
        private Client client;

        @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private Set<Transaction> transactions = new HashSet<>();

        // Constructor vacío
        public Account() {
        }

        // Constructor con parámetros
        public Account(String number, LocalDateTime creationDate, double balance, boolean status) {
            this.number = number;
            this.creationDate = creationDate;
            this.balance = balance;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public Client getClient() {
            return client;
        }

        public void setClient(Client client) {
            this.client = client;
        }

        public Set<Transaction> getTransactions() {
            return transactions;
        }

        public void setTransactions(Set<Transaction> transactions) {
            this.transactions = transactions;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "id=" + id +
                    ", number='" + number + '\'' +
                    ", creationDate=" + creationDate +
                    ", balance=" + balance +
                    ", status=" + status +
                    ", client=" + client +
                    ", transactions=" + transactions +
                    '}';
        }

        public void addTransaction(Transaction transaction) {
            transactions.add(transaction);
            transaction.setAccount(this);
        }
    }