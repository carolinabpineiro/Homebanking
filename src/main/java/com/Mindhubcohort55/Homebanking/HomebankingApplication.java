package com.Mindhubcohort55.Homebanking;

import com.Mindhubcohort55.Homebanking.models.*;
import com.Mindhubcohort55.Homebanking.repositories.*;
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

	@Autowired
	public HomebankingApplication(PasswordEncoder passwordEncoder,
								  ClientRepository clientRepository,
								  AccountRepository accountRepository,
								  TransactionRepository transactionRepository,
								  LoanRepository loanRepository,
								  ClientLoanRepository clientLoanRepository,
								  CardRepository cardRepository) {
		this.passwordEncoder = passwordEncoder;
		this.clientRepository = clientRepository;
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.loanRepository = loanRepository;
		this.clientLoanRepository = clientLoanRepository;
		this.cardRepository = cardRepository;
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
			lola.addAccounts(accountLola1);
			lola.addAccounts(accountLola2);

			// Transacciones para el usuario Lola
			addTransactions(accountLola1, new Transaction(TransactionType.DEBIT, -100.00, "Internet payment", LocalDateTime.now(), accountLola1));
			addTransactions(accountLola2, new Transaction(TransactionType.CREDIT, 500.00, "Salary payment", LocalDateTime.now(), accountLola2));

			clientRepository.save(lola);
			accountRepository.save(accountLola1);
			accountRepository.save(accountLola2);

			// Creación del usuario Melba
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1423"));
			Account accountMelba1 = new Account("VIN001", LocalDateTime.now(), 5000.00, true);
			Account accountMelba2 = new Account("VIN002", LocalDateTime.now(), 7500.00, true);
			melba.addAccounts(accountMelba1);
			melba.addAccounts(accountMelba2);

			addTransactions(accountMelba1, new Transaction(TransactionType.DEBIT, -200.00, "Taxes payment", LocalDateTime.now(), accountMelba1));
			addTransactions(accountMelba2, new Transaction(TransactionType.CREDIT, 234.00, "Refund", LocalDateTime.now(), accountMelba2));

			clientRepository.save(melba);
			accountRepository.save(accountMelba1);
			accountRepository.save(accountMelba2);

			// Creación del usuario Admin
			Client admin = new Client("Thomas", "Maldonado", "totomaldopi@gmail.com", passwordEncoder.encode("adminpassword"));
			Account adminAccount = new Account("VIN999", LocalDateTime.now(), 1000000.00, true);
			admin.addAccounts(adminAccount);

			clientRepository.save(admin);
			accountRepository.save(adminAccount);

			// Préstamos disponibles
			Loan mortgage = new Loan("Mortgage", 500000.00, Arrays.asList(12, 24, 36, 48, 60, 72));
			Loan personal = new Loan("Personal", 100000.00, Arrays.asList(6, 12, 24));
			Loan automotive = new Loan("Automotive", 300000.00, Arrays.asList(6, 12, 24, 36));

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			// Ejemplo de uso de tarjetas
			Card cardGold = new Card(CardType.DEBIT, CardColor.GOLD, CardNumberGenerator.makeCardNumber(), CvvGenerator.cvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5), "Card Holder Name", melba);
			melba.addCard(cardGold);
			cardRepository.save(cardGold);
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