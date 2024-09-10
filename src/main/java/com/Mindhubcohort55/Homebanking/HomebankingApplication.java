package com.Mindhubcohort55.Homebanking;

import com.Mindhubcohort55.Homebanking.models.*;
import com.Mindhubcohort55.Homebanking.repositories.*;
import com.Mindhubcohort55.Homebanking.services.ClientLoanService;
import com.Mindhubcohort55.Homebanking.utils.CardNumberGenerator;
import com.Mindhubcohort55.Homebanking.utils.CvvGenerator;
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

	private final PasswordEncoder passwordEncoder;
	private final ClientRepository clientRepository;
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final LoanRepository loanRepository;
	private final ClientLoanRepository clientLoanRepository;
	private final CardRepository cardRepository;
	private final ClientLoanService clientLoanService;

	@Autowired
	public HomebankingApplication(PasswordEncoder passwordEncoder,
								  ClientRepository clientRepository,
								  AccountRepository accountRepository,
								  TransactionRepository transactionRepository,
								  LoanRepository loanRepository,
								  ClientLoanRepository clientLoanRepository,
								  CardRepository cardRepository,
								  ClientLoanService clientLoanService) {
		this.passwordEncoder = passwordEncoder;
		this.clientRepository = clientRepository;
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.loanRepository = loanRepository;
		this.clientLoanRepository = clientLoanRepository;
		this.cardRepository = cardRepository;
		this.clientLoanService = clientLoanService;
	}

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData() {
		return args -> {
			// Creación del usuario Lola
			Client lola = new Client("Lola", "Pineiro", "lolapineiro@gmail.com", passwordEncoder.encode("0403"));
			Account accountLola1 = new Account("VIN003", LocalDateTime.now(), 100000.00, true);
			Account accountLola2 = new Account("VIN004", LocalDateTime.now(), 200000.00, true);
			lola.addAccount(accountLola1);
			lola.addAccount(accountLola2);

			// Transacciones para el usuario Lola
			addTransactions(accountLola1, new Transaction(TransactionType.DEBIT, -100.00, "Internet payment", LocalDateTime.now(), accountLola1));
			addTransactions(accountLola2, new Transaction(TransactionType.CREDIT, 500.00, "Salary payment", LocalDateTime.now(), accountLola2));

			// Préstamos para Lola
			Loan personalLoan = loanRepository.findById(1L).orElse(null); // Suponiendo que el ID del préstamo personal es 1
			if (personalLoan != null) {
				ClientLoan clientLoanLola = new ClientLoan(5000.00, 12);
				clientLoanLola.setClient(lola);
				clientLoanLola.setLoan(personalLoan);
				clientLoanService.saveClientLoan(clientLoanLola);
			}

			clientRepository.save(lola);
			accountRepository.save(accountLola1);
			accountRepository.save(accountLola2);

			// Creación del usuario Melba
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1423"));
			Account accountMelba1 = new Account("VIN001", LocalDateTime.now(), 5000.00, true);
			Account accountMelba2 = new Account("VIN002", LocalDateTime.now(), 7500.00, true);
			melba.addAccount(accountMelba1);
			melba.addAccount(accountMelba2);

			// Transacciones para el usuario Melba
			addTransactions(accountMelba1, new Transaction(TransactionType.DEBIT, -200.00, "Taxes payment", LocalDateTime.now(), accountMelba1));
			addTransactions(accountMelba2, new Transaction(TransactionType.CREDIT, 234.00, "Refund", LocalDateTime.now(), accountMelba2));

			// Préstamos para Melba
			Loan mortgageLoan = loanRepository.findById(1L).orElse(null); // Suponiendo que el ID del préstamo hipotecario es 1
			if (mortgageLoan != null) {
				ClientLoan clientLoanMelba = new ClientLoan(20000.00, 24);
				clientLoanMelba.setClient(melba);
				clientLoanMelba.setLoan(mortgageLoan);
				clientLoanService.saveClientLoan(clientLoanMelba);
			}

			clientRepository.save(melba);
			accountRepository.save(accountMelba1);
			accountRepository.save(accountMelba2);

			// Creación del usuario Admin
			Client admin = new Client("Thomas", "Maldonado", "totomaldopi@gmail.com", passwordEncoder.encode("adminpassword"));
			Account adminAccount = new Account("VIN999", LocalDateTime.now(), 1000000.00, true);
			admin.addAccount(adminAccount);

			// Transacciones para el usuario Admin
			addTransactions(adminAccount, new Transaction(TransactionType.CREDIT, 1000000.00, "Initial deposit", LocalDateTime.now(), adminAccount));

			// Préstamos para Admin
			Loan automotiveLoan = loanRepository.findById(2L).orElse(null); // Suponiendo que el ID del préstamo automotriz es 2
			if (automotiveLoan != null) {
				ClientLoan clientLoanAdmin = new ClientLoan(100000.00, 36);
				clientLoanAdmin.setClient(admin);
				clientLoanAdmin.setLoan(automotiveLoan);
				clientLoanService.saveClientLoan(clientLoanAdmin);
			}

			clientRepository.save(admin);
			accountRepository.save(adminAccount);

			// Ahora que los clientes están guardados, podemos guardar las tarjetas
			Card cardGold = new Card(CardType.DEBIT, CardColor.GOLD, CardNumberGenerator.makeCardNumber(), CvvGenerator.cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", melba);
			melba.addCard(cardGold);
			cardRepository.save(cardGold);

			Card cardTitanium = new Card(CardType.CREDIT, CardColor.TITANIUM, CardNumberGenerator.makeCardNumber(), CvvGenerator.cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", admin);
			admin.addCard(cardTitanium);
			cardRepository.save(cardTitanium);

			// Creación de clientes adicionales
			Client mariano = new Client("Mariano", "Filip", "mariano.filip@example.com", passwordEncoder.encode("password1"));
			Account accountMariano1 = new Account("VIN005", LocalDateTime.now(), 30000.00, true);
			Account accountMariano2 = new Account("VIN006", LocalDateTime.now(), 40000.00, true);
			mariano.addAccount(accountMariano1);
			mariano.addAccount(accountMariano2);

			// Transacciones para Mariano
			addTransactions(accountMariano1, new Transaction(TransactionType.CREDIT, 1500.00, "Deposit", LocalDateTime.now(), accountMariano1));
			addTransactions(accountMariano2, new Transaction(TransactionType.DEBIT, -500.00, "Withdrawal", LocalDateTime.now(), accountMariano2));

			// Préstamos para Mariano
			Loan personalLoan2 = loanRepository.findById(2L).orElse(null); // Suponiendo que el ID del préstamo personal es 2
			if (personalLoan2 != null) {
				ClientLoan clientLoanMariano = new ClientLoan(15000.00, 12);
				clientLoanMariano.setClient(mariano);
				clientLoanMariano.setLoan(personalLoan2);
				clientLoanService.saveClientLoan(clientLoanMariano);
			}

			// Guardar a Mariano y las cuentas
			clientRepository.save(mariano);
			accountRepository.save(accountMariano1);
			accountRepository.save(accountMariano2);

			// Tarjetas para Mariano
			Card cardSilver = new Card(CardType.DEBIT, CardColor.SILVER, CardNumberGenerator.makeCardNumber(), CvvGenerator.cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", mariano);
			mariano.addCard(cardSilver);
			cardRepository.save(cardSilver);

			Client dina = new Client("Dina", "Otra", "dina.otra@example.com", passwordEncoder.encode("password2"));
			Account accountDina1 = new Account("VIN007", LocalDateTime.now(), 50000.00, true);
			Account accountDina2 = new Account("VIN008", LocalDateTime.now(), 60000.00, true);
			dina.addAccount(accountDina1);
			dina.addAccount(accountDina2);

			// Transacciones para Dina
			addTransactions(accountDina1, new Transaction(TransactionType.CREDIT, 2000.00, "Deposit", LocalDateTime.now(), accountDina1));
			addTransactions(accountDina2, new Transaction(TransactionType.DEBIT, -1000.00, "Purchase", LocalDateTime.now(), accountDina2));

			// Préstamos para Dina
			Loan mortgageLoan2 = loanRepository.findById(3L).orElse(null); // Suponiendo que el ID del préstamo hipotecario es 3
			if (mortgageLoan2 != null) {
				ClientLoan clientLoanDina = new ClientLoan(25000.00, 24);
				clientLoanDina.setClient(dina);
				clientLoanDina.setLoan(mortgageLoan2);
				clientLoanService.saveClientLoan(clientLoanDina);
			}

			// Guardar a Dina y las cuentas
			clientRepository.save(dina);
			accountRepository.save(accountDina1);
			accountRepository.save(accountDina2);

			// Tarjetas para Dina
			Card cardGoldDina = new Card(CardType.CREDIT, CardColor.GOLD, CardNumberGenerator.makeCardNumber(), CvvGenerator.cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", dina);
			dina.addCard(cardGoldDina);
			cardRepository.save(cardGoldDina);

			Client luis = new Client("Luis", "Gimen", "luis.gimen@example.com", passwordEncoder.encode("password3"));
			Account accountLuis1 = new Account("VIN009", LocalDateTime.now(), 70000.00, true);
			Account accountLuis2 = new Account("VIN010", LocalDateTime.now(), 80000.00, true);
			luis.addAccount(accountLuis1);
			luis.addAccount(accountLuis2);

			// Transacciones para Luis
			addTransactions(accountLuis1, new Transaction(TransactionType.DEBIT, -1500.00, "Utilities payment", LocalDateTime.now(), accountLuis1));
			addTransactions(accountLuis2, new Transaction(TransactionType.CREDIT, 3000.00, "Bonus", LocalDateTime.now(), accountLuis2));

			// Préstamos para Luis
			Loan automotiveLoan2 = loanRepository.findById(3L).orElse(null); // Suponiendo que el ID del préstamo automotriz es 3
			if (automotiveLoan2 != null) {
				ClientLoan clientLoanLuis = new ClientLoan(20000.00, 24);
				clientLoanLuis.setClient(luis);
				clientLoanLuis.setLoan(automotiveLoan2);
				clientLoanService.saveClientLoan(clientLoanLuis);
			}

			// Guardar a Luis y las cuentas
			clientRepository.save(luis);
			accountRepository.save(accountLuis1);
			accountRepository.save(accountLuis2);

			// Tarjetas para Luis
			Card cardTitaniumLuis = new Card(CardType.DEBIT, CardColor.TITANIUM, CardNumberGenerator.makeCardNumber(), CvvGenerator.cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", luis);
			luis.addCard(cardTitaniumLuis);
			cardRepository.save(cardTitaniumLuis);
		};
	}

	// Método para simplificar la creación de transacciones y asociarlas a la cuenta
	private void addTransactions(Account account, Transaction... transactions) {
		for (Transaction transaction : transactions) {
			transaction.setAccount(account); // Establece la cuenta en la transacción
			account.addTransaction(transaction);
		}
	}
}