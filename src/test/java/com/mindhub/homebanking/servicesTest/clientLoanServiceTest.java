package com.mindhub.homebanking.servicesTest;

import com.Mindhubcohort55.Homebanking.models.ClientLoan;
import com.Mindhubcohort55.Homebanking.repositories.ClientLoanRepository;
import com.Mindhubcohort55.Homebanking.services.impl.ClientLoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class clientLoanServiceTest {

    @Mock
    private ClientLoanRepository clientLoanRepository;

    @InjectMocks
    private ClientLoanServiceImpl clientLoanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveClientLoan() {
        // Crear un objeto ClientLoan para la prueba
        ClientLoan clientLoan = new ClientLoan();


        // Ejecutar el método a probar
        clientLoanService.saveClientLoan(clientLoan);

    }
}
