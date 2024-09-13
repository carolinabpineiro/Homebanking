package com.Mindhubcohort55.Homebanking;

import com.Mindhubcohort55.Homebanking.models.*;
import com.Mindhubcohort55.Homebanking.repositories.*;
import com.Mindhubcohort55.Homebanking.services.ClientLoanService;
import com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator;
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


	//aca arranca la app
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);   //le cedo el control a spring
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	// Método que se ejecuta al iniciar la aplicación. esta en el contexto de spring, porq no teiene anotaciones
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository, ClientLoanService clientLoanService) { //ini, t data es el nombre del metodo 	//inyeccion de dependencias. corredor de comandos en linea
		return (args) -> {

			//crear prestamos
			Loan mortgage = new Loan("Mortgage", 500.000, Arrays.asList(12, 24, 36, 48, 60, 72));
			Loan personal = new Loan("Personal", 100.000,  Arrays.asList(6,12,24));
			Loan automotive = new Loan("Automotive", 300.000, Arrays.asList(6,12,24,36));
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);


			// Creación del usuario Lola
			Client lola = new Client("Lola", "Pineiro", "lolapineiro@gmail.com", passwordEncoder.encode("0403"));
			Account accountLola1 = new Account("VIN003", LocalDateTime.now(), 100000.00, true);
			Account accountLola2 = new Account("VIN004", LocalDateTime.now(), 200000.00, true);
			lola.addAccount(accountLola1);
			lola.addAccount(accountLola2);


			clientRepository.save(lola);
			accountRepository.save(accountLola1);
			accountRepository.save(accountLola2);

			// Creación del usuario Melba
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1423"));
			Account accountMelba1 = new Account("VIN001", LocalDateTime.now(), 5000.00, true);
			Account accountMelba2 = new Account("VIN002", LocalDateTime.now(), 7500.00, true);
			melba.addAccount(accountMelba1);
			melba.addAccount(accountMelba2);



			clientRepository.save(melba);
			accountRepository.save(accountMelba1);
			accountRepository.save(accountMelba2);
			Transaction transactionMelba1 = new Transaction(TransactionType.CREDIT, 100000.00, "Initial deposit", LocalDateTime.now(), accountLola1);
			transactionRepository.save(transactionMelba1);
			Transaction transactionMelba2 = new Transaction(TransactionType.CREDIT, 100000.00, "Initial deposit", LocalDateTime.now(), accountMelba1);
			transactionRepository.save(transactionMelba2);


//			Client admin = new Client("Thomas", "Maldonado", "totomaldopi@gmail.com", passwordEncoder.encode("adminpassword"));
//			Account adminAccount = new Account("VIN999", LocalDateTime.now(), 1000000.00, true);
//			admin.addAccount(adminAccount);
//
//
//			addTransactions(adminAccount, new Transaction(TransactionType.CREDIT, 1000000.00, "Initial deposit", LocalDateTime.now(), adminAccount));
//
//
//			Loan automotiveLoan = loanRepository.findById(2L).orElse(null); // Suponiendo que el ID del préstamo automotriz es 2
//			if (automotiveLoan != null) {
//				ClientLoan clientLoanAdmin = new ClientLoan(100000.00, 36);
//				clientLoanAdmin.setClient(admin);
//				clientLoanAdmin.setLoan(automotiveLoan);
//				clientLoanService.saveClientLoan(clientLoanAdmin);
//			}
//
//			clientRepository.save(admin);
//			accountRepository.save(adminAccount);
//
//
//			Card cardGold = new Card(CardType.DEBIT, CardColor.GOLD, CardNumberGenerator.getRandomCardNumber(), CardNumberGenerator.getRandomCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", melba);
//			melba.addCard(cardGold);
//			cardRepository.save(cardGold);
//
//			Card cardTitanium = new Card(CardType.CREDIT, CardColor.TITANIUM, CardNumberGenerator.getRandomCardNumber(), CardNumberGenerator.getRandomCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", admin);
//			admin.addCard(cardTitanium);
//			cardRepository.save(cardTitanium);
//
//
//			Client mariano = new Client("Mariano", "Filip", "mariano.filip@example.com", passwordEncoder.encode("password1"));
//			Account accountMariano1 = new Account("VIN005", LocalDateTime.now(), 30000.00, true);
//			Account accountMariano2 = new Account("VIN006", LocalDateTime.now(), 40000.00, true);
//			mariano.addAccount(accountMariano1);
//			mariano.addAccount(accountMariano2);
//
//
//			addTransactions(accountMariano1, new Transaction(TransactionType.CREDIT, 1500.00, "Deposit", LocalDateTime.now(), accountMariano1));
//			addTransactions(accountMariano2, new Transaction(TransactionType.DEBIT, -500.00, "Withdrawal", LocalDateTime.now(), accountMariano2));
//
//
//			Loan personalLoan2 = loanRepository.findById(2L).orElse(null); // Suponiendo que el ID del préstamo personal es 2
//			if (personalLoan2 != null) {
//				ClientLoan clientLoanMariano = new ClientLoan(15000.00, 12);
//				clientLoanMariano.setClient(mariano);
//				clientLoanMariano.setLoan(personalLoan2);
//				clientLoanService.saveClientLoan(clientLoanMariano);
//			}
//
//
//			clientRepository.save(mariano);
//			accountRepository.save(accountMariano1);
//			accountRepository.save(accountMariano2);
//
//
//			Card cardSilver = new Card(CardType.DEBIT, CardColor.SILVER, CardNumberGenerator.getRandomCardNumber(), CardNumberGenerator.getRandomCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", mariano);
//			mariano.addCard(cardSilver);
//			cardRepository.save(cardSilver);
//
//			Client dina = new Client("Dina", "Otra", "dina.otra@example.com", passwordEncoder.encode("password2"));
//			Account accountDina1 = new Account("VIN007", LocalDateTime.now(), 50000.00, true);
//			Account accountDina2 = new Account("VIN008", LocalDateTime.now(), 60000.00, true);
//			dina.addAccount(accountDina1);
//			dina.addAccount(accountDina2);
//
//
//			addTransactions(accountDina1, new Transaction(TransactionType.CREDIT, 2000.00, "Deposit", LocalDateTime.now(), accountDina1));
//			addTransactions(accountDina2, new Transaction(TransactionType.DEBIT, -1000.00, "Purchase", LocalDateTime.now(), accountDina2));
//
//
//			Loan mortgageLoan2 = loanRepository.findById(3L).orElse(null); // Suponiendo que el ID del préstamo hipotecario es 3
//			if (mortgageLoan2 != null) {
//				ClientLoan clientLoanDina = new ClientLoan(25000.00, 24);
//				clientLoanDina.setClient(dina);
//				clientLoanDina.setLoan(mortgageLoan2);
//				clientLoanService.saveClientLoan(clientLoanDina);
//			}
//
//
//			clientRepository.save(dina);
//			accountRepository.save(accountDina1);
//			accountRepository.save(accountDina2);
//
//
//			Card cardGoldDina = new Card(CardType.CREDIT, CardColor.GOLD, CardNumberGenerator.getRandomCardNumber(), CardNumberGenerator.getRandomCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", dina);
//			dina.addCard(cardGoldDina);
//			cardRepository.save(cardGoldDina);
//
//			Client luis = new Client("Luis", "Gimen", "luis.gimen@example.com", passwordEncoder.encode("password3"));
//			Account accountLuis1 = new Account("VIN009", LocalDateTime.now(), 70000.00, true);
//			Account accountLuis2 = new Account("VIN010", LocalDateTime.now(), 80000.00, true);
//			luis.addAccount(accountLuis1);
//			luis.addAccount(accountLuis2);
//
//
//			addTransactions(accountLuis1, new Transaction(TransactionType.DEBIT, -1500.00, "Utilities payment", LocalDateTime.now(), accountLuis1));
//			addTransactions(accountLuis2, new Transaction(TransactionType.CREDIT, 3000.00, "Bonus", LocalDateTime.now(), accountLuis2));
//
//
//			Loan automotiveLoan2 = loanRepository.findById(3L).orElse(null); // Suponiendo que el ID del préstamo automotriz es 3
//			if (automotiveLoan2 != null) {
//				ClientLoan clientLoanLuis = new ClientLoan(20000.00, 24);
//				clientLoanLuis.setClient(luis);
//				clientLoanLuis.setLoan(automotiveLoan2);
//				clientLoanService.saveClientLoan(clientLoanLuis);
//			}
//
//
//			clientRepository.save(luis);
//			accountRepository.save(accountLuis1);
//			accountRepository.save(accountLuis2);
//
//
//			Card cardTitaniumLuis = new Card(CardType.DEBIT, CardColor.TITANIUM, CardNumberGenerator.getRandomCardNumber(), CardNumberGenerator.getRandomCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", luis);
//			luis.addCard(cardTitaniumLuis);
//			cardRepository.save(cardTitaniumLuis);
		};
	}


	}

