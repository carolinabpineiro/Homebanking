package com.Mindhubcohort55.Homebanking.services.implement;

import com.Mindhubcohort55.Homebanking.dtos.LoanAplicationDTO;
import com.Mindhubcohort55.Homebanking.dtos.LoanDTO;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Loan;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.LoanRepository;
import com.Mindhubcohort55.Homebanking.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServicesImpl implements LoanServices {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public List<LoanDTO> getAllLoanDTO() {
        return getAllLoans().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public Loan applyLoan(Authentication authentication, LoanAplicationDTO loanDTO) {
        Client client = clientRepository.findByEmail(authentication.getName());

        // Crear un nuevo objeto Loan con los datos del DTO
        Loan loan = new Loan();
        loan.setName("Unspecified"); // Establecer un nombre predeterminado o recuperar uno apropiado
        loan.setMaxAmount(loanDTO.amount());
        loan.setPayments(Collections.singletonList(loanDTO.payments())); // Si los pagos están en una lista

        // Agregar lógica adicional aquí según sea necesario, como asignar el préstamo al cliente o validar datos

        // Guardar el préstamo en la base de datos
        loanRepository.save(loan);

        return loan; // Retorna el préstamo creado
    }
}