package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Obtener todos los clientes
    @GetMapping("/")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        // Aquí usamos el método del segundo código para obtener todos los clientes
        List<ClientDto> allClientsDto = clientService.getClientsDTO();
        return new ResponseEntity<>(allClientsDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        // Primero obtenemos el cliente por ID
        Client client = clientService.findClientById(id);

        if (client == null) {
            // Si el cliente no existe, devolvemos un 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Si el cliente existe, obtenemos su DTO
            ClientDto clientDto = clientService.getClientDTO(id);
            return new ResponseEntity<>(clientDto, HttpStatus.OK);
        }
    }

}