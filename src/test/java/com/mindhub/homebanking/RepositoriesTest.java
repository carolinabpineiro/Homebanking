package com.mindhub.homebanking;

import com.Mindhubcohort55.Homebanking.models.Loan;
import com.Mindhubcohort55.Homebanking.repositories.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {

    @Autowired
    private LoanRepository loanRepository;

    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void existPersonalLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void testMortgageLoanAmount() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Mortgage"))));
        Loan mortgageLoan = loans.stream()
                .filter(loan -> loan.getName().equals("Mortgage"))
                .findFirst()
                .orElse(null);
        assertThat(mortgageLoan, is(notNullValue()));
        assertThat(mortgageLoan.getMaxAmount(), is(equalTo(500000.00)));
    }

    @Test
    public void testAutomotiveLoanTerms() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Automotive"))));
        Loan automotiveLoan = loans.stream()
                .filter(loan -> loan.getName().equals("Automotive"))
                .findFirst()
                .orElse(null);
        assertThat(automotiveLoan, is(notNullValue()));
        assertThat(automotiveLoan.getPayments(), hasItems(6, 12, 24, 36));
    }
}