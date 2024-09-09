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

    // Repositorio para gestionar operaciones sobre clientes
    @Autowired
    private ClientRepository clientRepository;

    /**
     * Obtiene todos los clientes en formato DTO.
     *
     * @return Una lista de DTOs de clientes.
     */
    @Override
    public List<ClientDto> getClientsDTO() {
        // Obtener todos los clientes, mapearlos a DTOs y recogerlos en una lista
        return clientRepository.findAll().stream().map(ClientDto::new).collect(Collectors.toList());
    }

    /**
     * Obtiene el cliente actualmente autenticado.
     *
     * @param authentication Información de autenticación del usuario.
     * @return El cliente autenticado, basado en el email proporcionado en la autenticación.
     */
    @Override
    public Client getClientCurrent(Authentication authentication) {
        // Obtener cliente autenticado por email
        return clientRepository.findByEmail(authentication.getName());
    }

    /**
     * Obtiene un cliente en formato DTO por su ID.
     *
     * @param id ID del cliente.
     * @return El DTO del cliente encontrado, o null si no existe.
     */
    @Override
    public ClientDto getClientDTO(Long id) {
        return clientRepository.findById(id).map(ClientDto::new).orElse(null);
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id ID del cliente.
     * @return El cliente encontrado, o null si no existe.
     */
    @Override
    public Client findClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    /**
     * Método vacío para guardar un cliente. (No implementado en esta versión).
     *
     * @param client El cliente a guardar.
     */
    @Override
    public void saveCLient(Client client) {
        // Este método no está implementado
    }

    /**
     * Guarda un cliente en la base de datos.
     *
     * @param client El cliente a guardar.
     */
    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    /**
     * Obtiene un cliente por su email.
     *
     * @param email Email del cliente.
     * @return El cliente encontrado, o null si no existe.
     */
    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}