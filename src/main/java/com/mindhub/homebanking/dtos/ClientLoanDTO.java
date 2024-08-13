package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String loanName;
    private double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    // Getters y setters

    public Long getId() {
        return id;
    }


    public Long getLoanId() {
        return loanId;
    }


    public String getLoanName() {
        return loanName;
    }


    public double getAmount() {
        return amount;
    }


    public int getPayments() {
        return payments;
    }

}
