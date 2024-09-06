package com.Mindhubcohort55.Homebanking.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class LoanApplicationDTO {
    private long id;

    @Min(value = 1, message = "Amount must be greater than or equal to 1")
    private double amount;

    @Min(value = 1, message = "Payment must be greater than or equal to 1")
    private int payment;

    @NotBlank(message = "Destination account number cannot be empty")
    private String destinationAccountNumber;

    public LoanApplicationDTO(){}

    public LoanApplicationDTO(long id, double amount, int payment, String destinationAccountNumber){
        this.id = id;
        this.amount = amount;
        this.payment = payment;
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayment() {
        return payment;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
}
