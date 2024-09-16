package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Set<Transaction> findByAccount(Account account);

    List<Transaction> findByDateBetweenAndAccountNumber(LocalDateTime dateInit, LocalDateTime dateEnd, String accountNumber);
}