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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData() {
		return (args) -> {
			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);

			// ***** Cliente Lola Pineiro *****
			Client client = new Client("Lola", "Pineiro", "lolapineiro@gmail.com", "password432");
			clientRepository.save(client);

			// Crear y guardar cuentas para Lola
			Account cuenta1Lola = new Account("VIN001", 500.0, LocalDateTime.now());
			Account cuenta2Lola = new Account("VIN002", 7500.0, LocalDateTime.now());
			Account cuenta3Lola = new Account("VIN019", 7500.0, LocalDateTime.now());

			cuenta1Lola.setClient(client);
			cuenta2Lola.setClient(client);
			cuenta3Lola.setClient(client);

			accountRepository.save(cuenta1Lola);
			accountRepository.save(cuenta2Lola);
			accountRepository.save(cuenta3Lola);

			// Crear y guardar transacciones para la cuenta 1 de Lola
			Transaction transaccion1Lola1 = new Transaction(TransactionType.CREDIT, 200.0, "Salary", LocalDateTime.now(), cuenta1Lola);
			Transaction transaccion2Lola1 = new Transaction(TransactionType.DEBIT, -100.0, "Groceries", LocalDateTime.now(), cuenta1Lola);
			Transaction transaccion3Lola1 = new Transaction(TransactionType.DEBIT, -50.0, "Utilities", LocalDateTime.now(), cuenta1Lola);

			transactionRepository.save(transaccion1Lola1);
			transactionRepository.save(transaccion2Lola1);
			transactionRepository.save(transaccion3Lola1);

			cuenta1Lola.addTransaction(transaccion1Lola1);
			cuenta1Lola.addTransaction(transaccion2Lola1);
			cuenta1Lola.addTransaction(transaccion3Lola1);

			// Crear y guardar transacciones para la cuenta 2 de Lola
			Transaction transaccion1Lola2 = new Transaction(TransactionType.CREDIT, 300.0, "Bonus", LocalDateTime.now(), cuenta2Lola);
			Transaction transaccion2Lola2 = new Transaction(TransactionType.DEBIT, -200.0, "Rent", LocalDateTime.now(), cuenta2Lola);
			Transaction transaccion3Lola2 = new Transaction(TransactionType.DEBIT, -150.0, "Utilities", LocalDateTime.now(), cuenta2Lola);

			transactionRepository.save(transaccion1Lola2);
			transactionRepository.save(transaccion2Lola2);
			transactionRepository.save(transaccion3Lola2);

			cuenta2Lola.addTransaction(transaccion1Lola2);
			cuenta2Lola.addTransaction(transaccion2Lola2);
			cuenta2Lola.addTransaction(transaccion3Lola2);

			// ***** Cliente Melba Morel *****
			Client melba = new Client("Melba", "Morel", "melbamorel@gmail.com", "password123");
			clientRepository.save(melba);

			// Crear y guardar cuentas para Melba
			Account cuenta1Melba = new Account("VIN003", 10000.0, LocalDateTime.now());
			Account cuenta2Melba = new Account("VIN004", 2000.0, LocalDateTime.now());
			Account cuenta3Melba = new Account("VIN010", 2000.0, LocalDateTime.now());

			cuenta1Melba.setClient(melba);
			cuenta2Melba.setClient(melba);
			cuenta3Melba.setClient(melba);

			accountRepository.save(cuenta1Melba);
			accountRepository.save(cuenta2Melba);
			accountRepository.save(cuenta3Melba);

			// Crear y guardar transacciones para la cuenta 1 de Melba
			Transaction transaccion1Melba1 = new Transaction(TransactionType.CREDIT, 5000.0, "Freelance Work", LocalDateTime.now(), cuenta1Melba);
			Transaction transaccion2Melba1 = new Transaction(TransactionType.DEBIT, -2000.0, "New Laptop", LocalDateTime.now(), cuenta1Melba);
			Transaction transaccion3Melba1 = new Transaction(TransactionType.DEBIT, -100.0, "Groceries", LocalDateTime.now(), cuenta1Melba);

			transactionRepository.save(transaccion1Melba1);
			transactionRepository.save(transaccion2Melba1);
			transactionRepository.save(transaccion3Melba1);

			cuenta1Melba.addTransaction(transaccion1Melba1);
			cuenta1Melba.addTransaction(transaccion2Melba1);
			cuenta1Melba.addTransaction(transaccion3Melba1);

			// Crear y guardar transacciones para la cuenta 2 de Melba
			Transaction transaccion1Melba2 = new Transaction(TransactionType.CREDIT, 1000.0, "Payment", LocalDateTime.now(), cuenta2Melba);
			Transaction transaccion2Melba2 = new Transaction(TransactionType.DEBIT, -500.0, "Rent", LocalDateTime.now(), cuenta2Melba);
			Transaction transaccion3Melba2 = new Transaction(TransactionType.DEBIT, -100.0, "Utilities", LocalDateTime.now(), cuenta2Melba);

			transactionRepository.save(transaccion1Melba2);
			transactionRepository.save(transaccion2Melba2);
			transactionRepository.save(transaccion3Melba2);

			cuenta2Melba.addTransaction(transaccion1Melba2);
			cuenta2Melba.addTransaction(transaccion2Melba2);
			cuenta2Melba.addTransaction(transaccion3Melba2);

			// Crear préstamos
			Loan personalLoan = new Loan("Personal Loan", 10000.0, Arrays.asList(12, 24, 36, 48));
			Loan carLoan = new Loan("Car Loan", 15000.0, Arrays.asList(12, 24, 36));

			// Guardar préstamos
			loanRepository.save(personalLoan);
			loanRepository.save(carLoan);

			// Crear y asignar préstamos a clientes
			ClientLoan clientLoan1 = new ClientLoan(10000.0, 24);
			clientLoan1.setClient(client);
			clientLoan1.setLoan(personalLoan);

			ClientLoan clientLoan2 = new ClientLoan(15000.0, 36);
			clientLoan2.setClient(melba);
			clientLoan2.setLoan(carLoan);

			// Guardar préstamos de clientes
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			// Agregar préstamos a clientes (si la relación es bidireccional)
			client.addClientLoan(clientLoan1);
			melba.addClientLoan(clientLoan2);

			// Actualizar clientes con sus préstamos
			clientRepository.save(client);
			clientRepository.save(melba);

			// ***** Tarjetas de Crédito *****
			Card card1 = new Card(
					"1234567812345678",
					123,
					"Lola Pineiro",
					LocalDate.of(2021, 8, 31), // Desde
					LocalDate.of(2026, 8, 31), // Hasta
					CardType.CREDIT,
					CardColor.GOLD,
					client // Cliente
			);

			Card card2 = new Card(
					"2345678923456789",
					234,
					"Melba Morel",
					LocalDate.of(2021, 8, 31), // Desde
					LocalDate.of(2026, 8, 31), // Hasta
					CardType.DEBIT,
					CardColor.SILVER,
					melba // Cliente
			);

			cardRepository.save(card1);
			cardRepository.save(card2);

			// ***** Cliente Thomas Maldonado *****
			Client thomas = new Client("Thomas", "Maldonado", "totomaldopi@gmail.com", "password123");
			clientRepository.save(thomas);

			// Crear y guardar cuentas para Thomas
			Account cuenta1Thomas = new Account("VIN005", 2000.0, LocalDateTime.now());
			Account cuenta2Thomas = new Account("VIN006", 3000.0, LocalDateTime.now());
			Account cuenta3Thomas = new Account("VIN007", 1500.0, LocalDateTime.now());

			cuenta1Thomas.setClient(thomas);
			cuenta2Thomas.setClient(thomas);
			cuenta3Thomas.setClient(thomas);

			accountRepository.save(cuenta1Thomas);
			accountRepository.save(cuenta2Thomas);
			accountRepository.save(cuenta3Thomas);

			// Crear y guardar transacciones para la cuenta 1 de Thomas
			Transaction transaccion1Thomas1 = new Transaction(TransactionType.CREDIT, 500.0, "Salary", LocalDateTime.now(), cuenta1Thomas);
			Transaction transaccion2Thomas1 = new Transaction(TransactionType.DEBIT, -200.0, "Groceries", LocalDateTime.now(), cuenta1Thomas);
			Transaction transaccion3Thomas1 = new Transaction(TransactionType.DEBIT, -100.0, "Utilities", LocalDateTime.now(), cuenta1Thomas);

			transactionRepository.save(transaccion1Thomas1);
			transactionRepository.save(transaccion2Thomas1);
			transactionRepository.save(transaccion3Thomas1);

			cuenta1Thomas.addTransaction(transaccion1Thomas1);
			cuenta1Thomas.addTransaction(transaccion2Thomas1);
			cuenta1Thomas.addTransaction(transaccion3Thomas1);

			// Crear y guardar transacciones para la cuenta 2 de Thomas
			Transaction transaccion1Thomas2 = new Transaction(TransactionType.CREDIT, 600.0, "Bonus", LocalDateTime.now(), cuenta2Thomas);
			Transaction transaccion2Thomas2 = new Transaction(TransactionType.DEBIT, -300.0, "Rent", LocalDateTime.now(), cuenta2Thomas);
			Transaction transaccion3Thomas2 = new Transaction(TransactionType.DEBIT, -200.0, "Utilities", LocalDateTime.now(), cuenta2Thomas);

			transactionRepository.save(transaccion1Thomas2);
			transactionRepository.save(transaccion2Thomas2);
			transactionRepository.save(transaccion3Thomas2);

			cuenta2Thomas.addTransaction(transaccion1Thomas2);
			cuenta2Thomas.addTransaction(transaccion2Thomas2);
			cuenta2Thomas.addTransaction(transaccion3Thomas2);

			// Crear préstamos para Thomas
			Loan thomasLoan = new Loan("Home Loan", 25000.0, Arrays.asList(12, 24, 36, 48, 60));

			// Guardar préstamo
			loanRepository.save(thomasLoan);

			// Crear y asignar préstamo a Thomas
			ClientLoan clientLoanThomas = new ClientLoan(25000.0, 36);
			clientLoanThomas.setClient(thomas);
			clientLoanThomas.setLoan(thomasLoan);

			// Guardar préstamo de cliente
			clientLoanRepository.save(clientLoanThomas);

			// Agregar préstamo a Thomas (si la relación es bidireccional)
			thomas.addClientLoan(clientLoanThomas);

			// Actualizar Thomas con su préstamo
			clientRepository.save(thomas);

			// Crear y guardar tarjeta de crédito para Thomas
			Card cardThomas = new Card(
					"3456789034567890",
					345,
					"Thomas Maldonado",
					LocalDate.of(2022, 8, 31), // Desde
					LocalDate.of(2027, 8, 31), // Hasta
					CardType.CREDIT,
					CardColor.TITANIUM,
					thomas // Cliente
			);

			cardRepository.save(cardThomas);
		};
	}

}

