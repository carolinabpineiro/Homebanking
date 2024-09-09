package com.Mindhubcohort55.Homebanking.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // ID único del préstamo, generado automáticamente

    private String name; // Nombre del préstamo

    private double maxAmount;  // Monto máximo del préstamo, asegurado de tipo double

    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments = new ArrayList<>(); // Lista de pagos asociados al préstamo

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>(); // Conjunto de préstamos asociados a clientes

    // Constructores
    public Loan() {
        // Constructor vacío para inicialización
    }

    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name; // Nombre del préstamo
        this.maxAmount = maxAmount; // Monto máximo del préstamo
        this.payments = payments; // Lista de pagos asociados al préstamo
    }

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }
}