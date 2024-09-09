package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Encuentra un cliente por su email.
     *
     * @param email Email del cliente.
     * @return El cliente encontrado, o null si no existe.
     */
    Client findByEmail(String email);

    /**
     * Verifica si existe un cliente con el email dado.
     *
     * @param email Email del cliente.
     * @return true si existe un cliente con ese email, false en caso contrario.
     */
    Boolean existsByEmail(String email);
}