package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double maxAmount;

    @ElementCollection //anotacion q indica q payments es una coleccion de elementos basicos,se almacenara en una tabla separada en la db y se vinvula a la entidad loan
    @Column(name = "payments")
    private List<Integer> payments = new ArrayList<>(); //(lista de enteros)

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private List<ClientLoan> clientLoans = new ArrayList<>();

    // Constructor
    public Loan() {}

    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        this.clientLoans.add(clientLoan); //lista de prestamos asociada al cliente, el add agrega el objeto clientLoan a la lista de prestamos
        clientLoan.setLoan(this); //el setLoan establece la relacion entre la entidad loan y el objeto clientLoan. bidireccional, ClientLoan sabe a q Loan pertenece y al reves
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                ", payments=" + payments +
                ", clientLoans=" + clientLoans +
                '}';
    }
}

