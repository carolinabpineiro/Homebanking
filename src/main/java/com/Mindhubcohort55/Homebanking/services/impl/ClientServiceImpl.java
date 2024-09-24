package com.Mindhubcohort55.Homebanking.services.impl;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDto> getClientsDTO() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(ClientDto::new).collect(Collectors.toList());
    }

    @Override
    public ClientDto getClientCurrent(Authentication authentication) {
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email);
        return client != null ? new ClientDto(client) : null;
    }

    @Override
    public ClientDto getClientDTO(Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        return client != null ? new ClientDto(client) : null;
    }

    @Override
    public Client findClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}