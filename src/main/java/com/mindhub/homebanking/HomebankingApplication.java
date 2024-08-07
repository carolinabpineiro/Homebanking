package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository) {
		return (args) -> {
			// save a couple of clients
			repository.save(new Client("Mabel", "Morel", "melba@mindhub.com"));
			repository.save(new Client("Thomas", "Maldonado", "totomaldopi@gmail.com"));
			repository.save(new Client("Dina", "Orta", "dinao@gmail.com"));

		};
	}

}
