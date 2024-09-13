package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByAccountNumber(String number);
    List<Account> findByOwner (Client owner);
    List<Account> findByClientId(Long clientId);
    Boolean existsByIdAndOwner(Long accountId, Client client);
    Account findByNumber(String number);
}
