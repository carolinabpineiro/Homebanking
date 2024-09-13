package com.Mindhubcohort55.Homebanking.models;

import jakarta.persistence.*;


@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private int payments;
//fetch como se cargara la relacion cuando se recupere la entidad en la db
    @ManyToOne(fetch = FetchType.EAGER)  //muchas instancias de esta entidad pueden estar relac con una unica instancia de CLIENT
    @JoinColumn(name = "client_id") //personalizo la columna de la db q se usara para la clave externa q establece la relacion entre 2 tablas
    private Client client;//relacion con la entidad, con su objeto asociado (client)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(double amount, int payments) {
        this.amount = amount;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
