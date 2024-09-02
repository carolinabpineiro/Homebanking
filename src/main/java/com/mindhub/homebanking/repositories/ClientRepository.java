package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Anotacion para que funcioine como respositorio, y se la considerara como a una base de datos
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByEmail(String email);
}
