package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.ClientDTO;
import com.Mindhubcohort55.Homebanking.models.Client;

import java.util.List;

public interface ClientServices {
    List<Client> getAllClient();
    List<ClientDTO> getAllClientDTO();
    Client getClientById(Long id);
    ClientDTO getClientDTO(Client client);
}