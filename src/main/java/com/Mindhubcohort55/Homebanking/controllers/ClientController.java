package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.ClientDto;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

//    @Autowired
//    private AccountRepository accountRepository;

    @GetMapping("/")
    public ResponseEntity<List<ClientDto>> getAllClients() {

        List<Client> allClients = clientRepository.findAll();
        List<ClientDto> allClientsDto = new ArrayList<>();
        allClientsDto = allClients.stream().map(ClientDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(allClientsDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {

        Optional<Client> clientById = clientRepository.findById(id);

        if (clientById.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            Client clientData = clientById.get();
            ClientDto clientDto = new ClientDto(clientData);
            return new ResponseEntity<>(clientDto, HttpStatus.OK);
        }
    }

}