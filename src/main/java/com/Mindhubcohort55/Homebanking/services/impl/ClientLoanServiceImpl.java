package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.models.ClientLoan;
import com.Mindhubcohort55.Homebanking.repositories.ClientLoanRepository;
import com.Mindhubcohort55.Homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    // Repositorio para gestionar operaciones sobre préstamos de clientes
    @Autowired
    ClientLoanRepository clientLoanRepository;

    /**
     * Guarda un préstamo de cliente en la base de datos.
     *
     * @param clientLoan El préstamo de cliente a guardar.
     */
    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        // Guardar el préstamo en la base de datos usando el repositorio
        clientLoanRepository.save(clientLoan);
    }
}



