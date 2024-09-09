package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.models.ClientLoan;

public interface ClientLoanService {

    /**
     * Guarda un préstamo de cliente en la base de datos.
     *
     * @param clientLoan El préstamo de cliente a guardar.
     */
    void saveClientLoan(ClientLoan clientLoan);
}
