package com.mindhub.homebanking.servicesTest;

import com.Mindhubcohort55.Homebanking.dtos.LoanApplicationDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.models.Loan;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.repositories.LoanRepository;
import com.Mindhubcohort55.Homebanking.services.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class loanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyForLoan() {
        // Crear un objeto LoanApplicationDto para la prueba
        LoanApplicationDto loanApplicationDto = new LoanApplicationDto();
        // Establecer propiedades en loanApplicationDto si es necesario

        // Simular el comportamiento del repositorio de clientes
        Client client = new Client();
        client.setEmail("test@example.com");
        when(clientRepository.findByEmail("test@example.com")).thenReturn(client); // Simular el retorno del cliente

        // Simular un préstamo existente
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setName("Personal Loan");
        loan.setMaxAmount(10000);
        when(loanRepository.findById(1L)).thenReturn(java.util.Optional.of(loan));

        // Asignar un ID de préstamo al LoanApplicationDto si es necesario
        // loanApplicationDto.setLoanId(1L); // si tu DTO requiere un ID de préstamo

        // Simular el comportamiento del método applyForLoan
        ResponseEntity<?> responseEntity = loanService.applyForLoan("test@example.com", loanApplicationDto);

    }
}
