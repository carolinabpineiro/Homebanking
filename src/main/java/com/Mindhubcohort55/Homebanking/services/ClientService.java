package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    List<ClientDto> getClientsDTO();
    Client getClientCurrent(Authentication authentication);
    ClientDto getClientDTO(Long id);
    Client findClientById (Long id);
    void saveCLient (Client client);

    void saveClient(Client client);

    Client getClientByEmail(String email);

}