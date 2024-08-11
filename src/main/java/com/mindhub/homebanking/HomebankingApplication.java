package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private ClientLoanRepository clientLoanRepository;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData() {
		return (args) -> {
			// Crear clientes
			Client client1 = new Client("Mabel", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Thomas", "Maldonado", "totomaldopi@gmail.com");
			Client client3 = new Client("Dina", "Orta", "dinao@gmail.com");

			clientRepository.saveAll(Arrays.asList(client1, client2, client3));

			// Crear cuentas para los clientes
			Account account1 = new Account("123456789", 1000.0, LocalDate.now(), client1);
			Account account2 = new Account("987654321", 2000.0, LocalDate.now(), client1);
			Account account3 = new Account("111213141", 1500.0, LocalDate.now(), client2);
			Account account4 = new Account("161718192", 2500.0, LocalDate.now(), client3);

			accountRepository.saveAll(Arrays.asList(account1, account2, account3, account4));

			// Crear transacciones para las cuentas
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 200.0, "Deposit", LocalDate.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -100.0, "Withdrawal", LocalDate.now(), account1);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 300.0, "Deposit", LocalDate.now(), account2);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -150.0, "Withdrawal", LocalDate.now(), account2);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 500.0, "Deposit", LocalDate.now(), account3);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -200.0, "Withdrawal", LocalDate.now(), account3);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 600.0, "Deposit", LocalDate.now(), account4);
			Transaction transaction8 = new Transaction(TransactionType.DEBIT, -300.0, "Withdrawal", LocalDate.now(), account4);

			transactionRepository.saveAll(Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6, transaction7, transaction8));

			// Crear préstamos
			Loan mortgage = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60));
			Loan personal = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			Loan automotive = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24, 36));

			loanRepository.saveAll(Arrays.asList(mortgage, personal, automotive));

			// Crear ClientLoan
			ClientLoan clientLoan1 = new ClientLoan(400000, 60, client1, mortgage);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, client1, personal);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, client2, personal);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36, client2, automotive);

			clientLoanRepository.saveAll(Arrays.asList(clientLoan1, clientLoan2, clientLoan3, clientLoan4));
		};
	}
}