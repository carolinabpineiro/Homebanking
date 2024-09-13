package com.Mindhubcohort55.Homebanking.dtos;

public record MakeTransactionDTO (String sourceAccount, String destinationAccount, Double amount, String description) {
}
