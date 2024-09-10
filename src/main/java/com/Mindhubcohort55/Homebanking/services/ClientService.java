package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    // Obtiene todos los clientes en formato DTO.
    List<ClientDto> getClientsDTO();

    // Obtiene el cliente autenticado actual.
    Client getClientCurrent(Authentication authentication);

    // Obtiene un cliente en formato DTO por su ID.
    ClientDto getClientDTO(Long id);

    // Busca un cliente por su ID.
    Client findClientById(Long id);

    // Guarda un cliente en la base de datos.
    void saveClient(Client client);

    // Obtiene un cliente por su email.
    Client getClientByEmail(String email);
}