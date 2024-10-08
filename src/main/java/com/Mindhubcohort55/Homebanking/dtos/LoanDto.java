package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDto {
    private long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();

    public LoanDto() {}

    public LoanDto(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}