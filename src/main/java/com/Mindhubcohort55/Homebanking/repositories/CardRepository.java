package com.Mindhubcohort55.Homebanking.repositories;

import com.Mindhubcohort55.Homebanking.models.Card;
import com.Mindhubcohort55.Homebanking.models.CardType;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Verifica si existe una tarjeta con el número dado.
     *
     * @param cardNumber Número de la tarjeta.
     * @return true si existe una tarjeta con ese número, false en caso contrario.
     */
    Boolean existsByCardNumber(String cardNumber);

    /**
     * Verifica si existe una tarjeta con el CVV dado.
     *
     * @param cvv Código de verificación de la tarjeta.
     * @return true si existe una tarjeta con ese CVV, false en caso contrario.
     */
    Boolean existsByCvv(String cvv);

    /**
     * Cuenta la cantidad de tarjetas asociadas con un cliente y tipo de tarjeta dado.
     *
     * @param client Cliente asociado con las tarjetas.
     * @param cardType Tipo de tarjeta (débito o crédito).
     * @return La cantidad de tarjetas del cliente con el tipo dado.
     */
    long countByClientAndCardType(Client client, CardType cardType);
}
