package com.Mindhubcohort55.Homebanking.dtos;

import com.Mindhubcohort55.Homebanking.models.ClientLoan;

public class ClientLoanDTO {
   private Long id;
   private Long loanid;
   private String name;
   private double amount;
   private int payments;


   public ClientLoanDTO(ClientLoan clientLoan) {
       this.id = clientLoan.getId();
       this.loanid = clientLoan.getLoan().getId();
       this.name = clientLoan.getLoan().getName();
       this.amount = clientLoan.getAmount();
       this.payments = clientLoan.getPayments();
   }

    public Long getId() {
        return id;
    }

    public Long getLoanid() {
        return loanid;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
}
