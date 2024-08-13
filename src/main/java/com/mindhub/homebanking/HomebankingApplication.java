package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication //Le cedo el control de la aplicación a Spring Boot
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
			Client melba = new Client("Melba", "Molina", "jTqFP@example.com");
			Client john = new Client("John", "Doe", "lVJZd@example.com");
			Client jane = new Client("Jane", "Doe", "lVJZd@example.com");

			clientRepository.save(melba);
			clientRepository.save(john);
			clientRepository.save(jane);

			// Crear y guardar las cuentas asociadas a Melba
			Account account1 = new Account("VIN001", 5000, LocalDateTime.now());
			Account account2 = new Account("VIN002", 7500, LocalDateTime.now());

			melba.addAccount(account1);
			melba.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);

			// Crear y guardar transacciones para las cuentas de Melba
			Transaction transaccion1Melba1 = new Transaction(
					TransactionType.CREDIT, 100.0, "Deposit", LocalDateTime.now(), account1);

			Transaction transaccion2Melba1 = new Transaction(
					TransactionType.DEBIT, -50.0, "Withdrawal", LocalDateTime.now(), account1);

			Transaction transaccion3Melba1 = new Transaction(
					TransactionType.CREDIT, 200.0, "Transfer", LocalDateTime.now(), account1);

			account1.addTransaction(transaccion1Melba1);
			account1.addTransaction(transaccion2Melba1);
			account1.addTransaction(transaccion3Melba1);
			transactionRepository.save(transaccion1Melba1);
			transactionRepository.save(transaccion2Melba1);
			transactionRepository.save(transaccion3Melba1);

			Transaction transaccion1Melba2 = new Transaction(
					TransactionType.CREDIT, 300.0, "Freelance Work", LocalDateTime.now(), account2);

			Transaction transaccion2Melba2 = new Transaction(
					TransactionType.DEBIT, -100.0, "Groceries", LocalDateTime.now(), account2);

			Transaction transaccion3Melba2 = new Transaction(
					TransactionType.CREDIT, 150.0, "Consulting Fee", LocalDateTime.now(), account2);

			account2.addTransaction(transaccion1Melba2);
			account2.addTransaction(transaccion2Melba2);
			account2.addTransaction(transaccion3Melba2);
			transactionRepository.save(transaccion1Melba2);
			transactionRepository.save(transaccion2Melba2);
			transactionRepository.save(transaccion3Melba2);

			// Crear y guardar cuentas para John
			Account account3 = new Account("VIN003", 10000, LocalDateTime.now());
			Account account4 = new Account("VIN004", 12000, LocalDateTime.now());

			john.addAccount(account3);
			john.addAccount(account4);
			accountRepository.save(account3);
			accountRepository.save(account4);

			// Crear y guardar transacciones para las cuentas de John
			Transaction transaccion1John1 = new Transaction(
					TransactionType.CREDIT, 500.0, "Salary", LocalDateTime.now(), account3);

			Transaction transaccion2John1 = new Transaction(
					TransactionType.DEBIT, -200.0, "Rent", LocalDateTime.now(), account3);

			Transaction transaccion3John1 = new Transaction(
					TransactionType.CREDIT, 300.0, "Stock Sale", LocalDateTime.now(), account3);

			account3.addTransaction(transaccion1John1);
			account3.addTransaction(transaccion2John1);
			account3.addTransaction(transaccion3John1);
			transactionRepository.save(transaccion1John1);
			transactionRepository.save(transaccion2John1);
			transactionRepository.save(transaccion3John1);

			Transaction transaccion1John2 = new Transaction(
					TransactionType.CREDIT, 700.0, "Bonus", LocalDateTime.now(), account4);

			Transaction transaccion2John2 = new Transaction(
					TransactionType.DEBIT, -100.0, "Utilities", LocalDateTime.now(), account4);

			Transaction transaccion3John2 = new Transaction(
					TransactionType.CREDIT, 400.0, "Freelance Work", LocalDateTime.now(), account4);

			account4.addTransaction(transaccion1John2);
			account4.addTransaction(transaccion2John2);
			account4.addTransaction(transaccion3John2);
			transactionRepository.save(transaccion1John2);
			transactionRepository.save(transaccion2John2);
			transactionRepository.save(transaccion3John2);

			// Crear y guardar cuentas para Jane
			Account account5 = new Account("VIN005", 15000, LocalDateTime.now());
			Account account6 = new Account("VIN006", 18000, LocalDateTime.now());

			jane.addAccount(account5);
			jane.addAccount(account6);
			accountRepository.save(account5);
			accountRepository.save(account6);

			// Crear y guardar transacciones para las cuentas de Jane
			Transaction transaccion1Jane1 = new Transaction(
					TransactionType.CREDIT, 800.0, "Savings", LocalDateTime.now(), account5);

			Transaction transaccion2Jane1 = new Transaction(
					TransactionType.DEBIT, -300.0, "Car Payment", LocalDateTime.now(), account5);

			Transaction transaccion3Jane1 = new Transaction(
					TransactionType.CREDIT, 500.0, "Bonus", LocalDateTime.now(), account5);

			account5.addTransaction(transaccion1Jane1);
			account5.addTransaction(transaccion2Jane1);
			account5.addTransaction(transaccion3Jane1);
			transactionRepository.save(transaccion1Jane1);
			transactionRepository.save(transaccion2Jane1);
			transactionRepository.save(transaccion3Jane1);

			Transaction transaccion1Jane2 = new Transaction(
					TransactionType.CREDIT, 1000.0, "Investment", LocalDateTime.now(), account6);

			Transaction transaccion2Jane2 = new Transaction(
					TransactionType.DEBIT, -200.0, "Groceries", LocalDateTime.now(), account6);

			Transaction transaccion3Jane2 = new Transaction(
					TransactionType.CREDIT, 700.0, "Consulting Fee", LocalDateTime.now(), account6);

			account6.addTransaction(transaccion1Jane2);
			account6.addTransaction(transaccion2Jane2);
			account6.addTransaction(transaccion3Jane2);
			transactionRepository.save(transaccion1Jane2);
			transactionRepository.save(transaccion2Jane2);
			transactionRepository.save(transaccion3Jane2);

			// Crear préstamos
			Loan mortgage = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60, 72));
			Loan personal = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			Loan automotive = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24, 36));
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			// Asignar préstamos a clientes
			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			melba.addClientLoan(clientLoan1);
			mortgage.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			melba.addClientLoan(clientLoan2);
			personal.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			jane.addClientLoan(clientLoan3);
			personal.addClientLoan(clientLoan3);
			clientLoanRepository.save(clientLoan3);

			ClientLoan clientLoan4 = new ClientLoan(200000, 36);
			jane.addClientLoan(clientLoan4);
			automotive.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan4);

		};
	}
}