package com.Mindhubcohort55.Homebanking.services;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    /**
     * Obtiene todos los clientes en formato DTO.
     *
     * @return Una lista de DTOs de clientes.
     */
    List<ClientDto> getClientsDTO();

    /**
     * Obtiene el cliente actualmente autenticado.
     *
     * @param authentication Información de autenticación del usuario.
     * @return El cliente autenticado, basado en el email proporcionado en la autenticación.
     */
    Client getClientCurrent(Authentication authentication);

    /**
     * Obtiene un cliente en formato DTO por su ID.
     *
     * @param id ID del cliente.
     * @return El DTO del cliente encontrado, o null si no existe.
     */
    ClientDto getClientDTO(Long id);

    /**
     * Busca un cliente por su ID.
     *
     * @param id ID del cliente.
     * @return El cliente encontrado, o null si no existe.
     */
    Client findClientById(Long id);

    /**
     * Guarda un cliente en la base de datos.
     *
     * @param client El cliente a guardar.
     */
    void saveCLient(Client client);

    /**
     * Guarda un cliente en la base de datos.
     *
     * @param client El cliente a guardar.
     */
    void saveClient(Client client);

    /**
     * Obtiene un cliente por su email.
     *
     * @param email Email del cliente.
     * @return El cliente encontrado, o null si no existe.
     */
    Client getClientByEmail(String email);
}