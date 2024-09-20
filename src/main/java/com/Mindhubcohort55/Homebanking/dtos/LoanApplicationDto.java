package com.Mindhubcohort55.Homebanking.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class LoanApplicationDto {

    // Atributo que almacena el ID del préstamo solicitado
    private long id;

    // Validación para que el monto sea mayor o igual a 1
    @Min(value = 1, message = "Amount must be greater than or equal to 1")
    private double amount;

    // Validación para que las cuotas de pago sean mayor o igual a 1
    @Min(value = 1, message = "Payments must be greater than or equal to 1")
    private int payments;

    // Validación para que el número de cuenta de destino no esté vacío
    @NotBlank(message = "Destination account number cannot be empty")
    private String destinationAccount;

    // Constructor vacío por defecto (necesario para algunas funcionalidades de Java Beans)
    public LoanApplicationDto() {}

    // Constructor que inicializa todos los atributos
    public LoanApplicationDto(long id, double amount, int payments, String destinationAccount) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

    // Getter para obtener el ID del préstamo
    public long getId() {
        return id;
    }

    // Setter para modificar el ID del préstamo
    public void setId(long id) {
        this.id = id;
    }

    // Getter para obtener el monto del préstamo
    public double getAmount() {
        return amount;
    }

    // Setter para modificar el monto del préstamo
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Getter para obtener el número de pagos (cuotas)
    public int getPayments() {
        return payments;
    }

    // Setter para modificar el número de pagos (cuotas)
    public void setPayments(int payments) {
        this.payments = payments;
    }

    // Getter para obtener el número de cuenta de destino
    public String getDestinationAccount() {
        return destinationAccount;
    }

    // Setter para modificar el número de cuenta de destino
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
}