package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Verifica si existe una cuenta con el número dado.
     *
     * @param number Número de cuenta.
     * @return true si existe una cuenta con ese número, false en caso contrario.
     */
    Boolean existsByNumber(String number);

    /**
     * Encuentra una cuenta por su número.
     *
     * @param number Número de cuenta.
     * @return La cuenta encontrada, o null si no existe.
     */
    Account findByNumber(String number);

    /**
     * Verifica si existe una cuenta con el ID y cliente dados.
     *
     * @param accountId ID de la cuenta.
     * @param client Cliente asociado con la cuenta.
     * @return true si existe una cuenta con ese ID y cliente, false en caso contrario.
     */
    Boolean existsByIdAndClient(Long accountId, Client client); // Cambiado de 'owner' a 'client'

    /**
     * Encuentra todas las cuentas asociadas con el cliente dado.
     *
     * @param client Cliente asociado con las cuentas.
     * @return Una lista de cuentas asociadas al cliente.
     */
    List<Account> findByClient(Client client);

    /**
     * Encuentra todas las cuentas con el estado dado.
     *
     * @param status Estado de la cuenta (true para activa, false para inactiva).
     * @return Una lista de cuentas con el estado dado.
     */
    List<Account> findByStatus(boolean status);
}