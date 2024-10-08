package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardColor;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumber(String cardNumber);

    boolean existsByCvv(String cvv);

    long countByClientAndCardType(Client client, CardType cardType);
    boolean existsByClientAndCardTypeAndCardColor(Client client, CardType cardType, CardColor cardColor);

    List<Card> findByClient(Client client);
}