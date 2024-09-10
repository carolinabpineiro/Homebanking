package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Boolean existsByNumber(String number);

    Account findByNumber(String number);

    Boolean existsByIdAndClient(Long accountId, Client client);

    List<Account> findByClient(Client client);

    List<Account> findByStatus(boolean status);
}