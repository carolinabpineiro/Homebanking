package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClientDto> getClientsDTO();
    ClientDto getClientCurrent(Authentication authentication); // Devuelve ClientDto del cliente autenticado
    ClientDto getClientDTO(Long id);
    Client findClientById(Long id); // Devuelve Client por ID
    void saveClient(Client client);
    Client getClientByEmail(String email); // Busca un cliente por email
}