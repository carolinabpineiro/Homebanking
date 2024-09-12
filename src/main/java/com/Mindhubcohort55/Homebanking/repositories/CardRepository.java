package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface CardRepository extends JpaRepository<Card, Long> {
    Set<Card> findByClient(Client client);
    boolean existsByCardNumber(String cardNumber);
    boolean existsByCvv(String cvv);
    long countByClientAndCardType(Client client, CardType cardType);
}
