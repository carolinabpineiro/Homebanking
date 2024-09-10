package com.Mindhubcohort55.Homebanking.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class LoanApplicationDTO {

    // Atributo que almacena el ID del préstamo solicitado
    private long id;

    // Validación para que el monto sea mayor o igual a 1
    @Min(value = 1, message = "Amount must be greater than or equal to 1")
    private double amount;

    // Validación para que las cuotas de pago sean mayor o igual a 1
    @Min(value = 1, message = "Payment must be greater than or equal to 1")
    private int payment;

    // Validación para que el número de cuenta de destino no esté vacío
    @NotBlank(message = "Destination account number cannot be empty")
    private String destinationAccountNumber;

    // Constructor vacío por defecto (necesario para algunas funcionalidades de Java Beans)
    public LoanApplicationDTO(){}

    // Constructor que inicializa todos los atributos
    public LoanApplicationDTO(long id, double amount, int payment, String destinationAccountNumber){
        this.id = id;
        this.amount = amount;
        this.payment = payment;
        this.destinationAccountNumber = destinationAccountNumber;
    }

    // Getter para obtener el ID del préstamo
    public long getId() {
        return id;
    }

    // Getter para obtener el monto del préstamo
    public double getAmount() {
        return amount;
    }

    // Getter para obtener el número de pagos (cuotas)
    public int getPayment() {
        return payment;
    }

    // Getter para obtener el número de cuenta de destino
    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
}