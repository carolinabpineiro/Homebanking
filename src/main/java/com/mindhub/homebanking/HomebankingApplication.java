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

			ClientDTO melbaDTO = new ClientDTO(melba);
			ClientDTO johnDTO = new ClientDTO(john);
			ClientDTO janeDTO = new ClientDTO(jane);

			clientRepository.save(melba);
			clientRepository.save(john);
			clientRepository.save(jane);

			// Crear y guardar las cuentas asociadas a Melba utilizando DTOs
			Account account1 = new Account("VIN001", 5000, LocalDateTime.now());
			Account account2 = new Account("VIN002", 7500, LocalDateTime.now());
			AccountDTO account1DTO = new AccountDTO(account1);
			AccountDTO account2DTO = new AccountDTO(account2);

			melba.addAccount(account1);
			melba.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);

			// Crear y guardar transacciones para las cuentas de Melba
			Transaction transaccion1Melba1 = new Transaction(
					TransactionType.CREDIT, 100.0, "Deposit", LocalDateTime.now(), null); // Pasa null o una instancia de Account

			Transaction transaccion2Melba1 = new Transaction(
					TransactionType.DEBIT, -50.0, "Withdrawal", LocalDateTime.now(), null); // Pasa null o una instancia de Account

			Transaction transaccion3Melba1 = new Transaction(
					TransactionType.CREDIT, 200.0, "Transfer", LocalDateTime.now(), null); // Pasa null o una instancia de Account
			account1.addTransaction(transaccion1Melba1);
			account1.addTransaction(transaccion2Melba1);
			account1.addTransaction(transaccion3Melba1);
			transactionRepository.save(transaccion1Melba1);
			transactionRepository.save(transaccion2Melba1);
			transactionRepository.save(transaccion3Melba1);

			Transaction transaccion1Melba2 = new Transaction(
					TransactionType.CREDIT, // Primero el tipo
					300.0, // Luego el monto
					"Freelance Work", // Luego la descripción
					LocalDateTime.now(), // Luego la fecha
					account2); // Finalmente la cuenta (puede ser null si no tienes una cuenta asignada todavía)

			Transaction transaccion2Melba2 = new Transaction(
					TransactionType.DEBIT, // Primero el tipo
					-100.0, // Luego el monto
					"Groceries", // Luego la descripción
					LocalDateTime.now(), // Luego la fecha
					account2); // Finalmente la cuenta

			Transaction transaccion3Melba2 = new Transaction(
					TransactionType.CREDIT, // Primero el tipo
					150.0, // Luego el monto
					"Consulting Fee", // Luego la descripción
					LocalDateTime.now(), // Luego la fecha
					account2); // Finalmente la cuenta

			account2.addTransaction(transaccion1Melba2);
			account2.addTransaction(transaccion2Melba2);
			account2.addTransaction(transaccion3Melba2);
			transactionRepository.save(transaccion1Melba2);
			transactionRepository.save(transaccion2Melba2);
			transactionRepository.save(transaccion3Melba2);

			// Crear y guardar cuentas para el segundo cliente (John)
			Account account3 = new Account("VIN003", 10000, LocalDateTime.now());
			Account account4 = new Account("VIN004", 12000, LocalDateTime.now());

			AccountDTO account3DTO = new AccountDTO(account3);
			AccountDTO account4DTO = new AccountDTO(account4);

			john.addAccount(account3);
			john.addAccount(account4);

			accountRepository.save(account3);
			accountRepository.save(account4);

			// Crear y guardar transacciones para las cuentas de John
			// Crear las transacciones con el orden correcto de parámetros
			Transaction transaccion1John1 = new Transaction(
					TransactionType.CREDIT, // Tipo
					500.0, // Monto
					"Salary", // Descripción
					LocalDateTime.now(), // Fecha
					account1); // Cuenta asociada (asegúrate de que `account1` esté definido)

			Transaction transaccion2John1 = new Transaction(
					TransactionType.DEBIT, // Tipo
					-200.0, // Monto
					"Rent", // Descripción
					LocalDateTime.now(), // Fecha
					account1); // Cuenta asociada

			Transaction transaccion3John1 = new Transaction(
					TransactionType.CREDIT, // Tipo
					300.0, // Monto
					"Stock Sale", // Descripción
					LocalDateTime.now(), // Fecha
					account1); // Cuenta asociada

			account3.addTransaction(transaccion1John1);
			account3.addTransaction(transaccion2John1);
			account3.addTransaction(transaccion3John1);
			transactionRepository.save(transaccion1John1);
			transactionRepository.save(transaccion2John1);
			transactionRepository.save(transaccion3John1);

			Transaction transaccion1John2 = new Transaction(
					TransactionType.CREDIT, // Tipo
					700.0, // Monto
					"Bonus", // Descripción
					LocalDateTime.now(), // Fecha
					account2); // Cuenta asociada

			Transaction transaccion2John2 = new Transaction(
					TransactionType.DEBIT, // Tipo
					-100.0, // Monto
					"Utilities", // Descripción
					LocalDateTime.now(), // Fecha
					account2); // Cuenta asociada

			Transaction transaccion3John2 = new Transaction(
					TransactionType.CREDIT, // Tipo
					400.0, // Monto
					"Freelance Work", // Descripción
					LocalDateTime.now(), // Fecha
					account2); // Cuenta asociada
			account4.addTransaction(transaccion1John2);
			account4.addTransaction(transaccion2John2);
			account4.addTransaction(transaccion3John2);
			transactionRepository.save(transaccion1John2);
			transactionRepository.save(transaccion2John2);
			transactionRepository.save(transaccion3John2);

			// Crear y guardar cuentas para el tercer cliente (Jane)

			Account account5 = new Account("VIN005", 15000, LocalDateTime.now());
			Account account6 = new Account("VIN006", 18000, LocalDateTime.now());


			AccountDTO account5DTO = new AccountDTO(account5);
			AccountDTO account6DTO = new AccountDTO(account6);

			jane.addAccount(account5);
			jane.addAccount(account6);

			accountRepository.save(account5);
			accountRepository.save(account6);

			// Crear y guardar transacciones para las cuentas de Jane
			Transaction transaccion1Jane1 = new Transaction(
					TransactionType.CREDIT, // Tipo
					800.0, // Monto
					"Savings", // Descripción
					LocalDateTime.now(), // Fecha
					account1); // Cuenta asociada

			Transaction transaccion2Jane1 = new Transaction(
					TransactionType.DEBIT, // Tipo
					-300.0, // Monto
					"Car Payment", // Descripción
					LocalDateTime.now(), // Fecha
					account1); // Cuenta asociada

			Transaction transaccion3Jane1 = new Transaction(
					TransactionType.CREDIT, // Tipo
					500.0, // Monto
					"Bonus", // Descripción
					LocalDateTime.now(), // Fecha
					account1); // Cuenta asociada
			account5.addTransaction(transaccion1Jane1);
			account5.addTransaction(transaccion2Jane1);
			account5.addTransaction(transaccion3Jane1);
			transactionRepository.save(transaccion1Jane1);
			transactionRepository.save(transaccion2Jane1);
			transactionRepository.save(transaccion3Jane1);

			Transaction transaccion1Jane2 = new Transaction(
					TransactionType.CREDIT, // Tipo
					1000.0, // Monto
					"Investment", // Descripción
					LocalDateTime.now(), // Fecha
					account2); // Cuenta asociada

			Transaction transaccion2Jane2 = new Transaction(
					TransactionType.DEBIT, // Tipo
					-200.0, // Monto
					"Groceries", // Descripción
					LocalDateTime.now(), // Fecha
					account2); // Cuenta asociada

			Transaction transaccion3Jane2 = new Transaction(
					TransactionType.CREDIT, // Tipo
					700.0, // Monto
					"Consulting Fee", // Descripción
					LocalDateTime.now(), // Fecha
					account2); // Cuenta asociada
			account6.addTransaction(transaccion1Jane2);
			account6.addTransaction(transaccion2Jane2);
			account6.addTransaction(transaccion3Jane2);
			transactionRepository.save(transaccion1Jane2);
			transactionRepository.save(transaccion2Jane2);
			transactionRepository.save(transaccion3Jane2);



			//crear prestamos
			Loan mortgage = new Loan("Mortgage", 500.000, Arrays.asList(12, 24, 36, 48, 60, 72));
			Loan personal = new Loan("Personal", 100.000,  Arrays.asList(6,12,24));
			Loan automotive = new Loan("Automotive", 300.000, Arrays.asList(6,12,24,36));
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);


			//crear prestamos a las cuentas

			ClientLoan clientLoan1 = new ClientLoan( 400.000, 60);
			melba.addClientLoan(clientLoan1);//aca melba aspira a ese prestamo
			mortgage.addClientLoan(clientLoan1);//aca le doy el prestamo
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan( 50.000, 12);
			melba.addClientLoan(clientLoan2);//aca melba aspira a ese prestamo
			personal.addClientLoan(clientLoan2);//aca le doy el prestamo
			clientLoanRepository.save(clientLoan2);


			ClientLoan clientLoan3 = new ClientLoan(100.000, 24);
			jane.addClientLoan(clientLoan3);
			personal.addClientLoan(clientLoan3);
			clientLoanRepository.save(clientLoan3);

			ClientLoan clientLoan4 = new ClientLoan(200.000, 36);
			jane.addClientLoan(clientLoan4);
			automotive.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan4);


		};
	}
}