package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/clients")
public class ClientsController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/hello")
    public String getClients() {
        return "Hello Clients";
    }

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id) {
        Optional<Client> clientById = clientRepository.findById(id);
        if (clientById.isEmpty()) {
            return new ResponseEntity<>("Client not found with id " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ClientDTO(clientById.get()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientDTO clientDTO) {
        if (clientDTO.getFirstName().isBlank() || clientDTO.getLastName().isBlank() || clientDTO.getEmail().isBlank()) {
            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }

        // Aquí deberías hash de la contraseña
        String hashedPassword = hashPassword("defaultPassword"); // Cambia esto según tu lógica de seguridad

        Client client = new Client(clientDTO.getFirstName(), clientDTO.getLastName(), clientDTO.getEmail(), hashedPassword);
        Client savedClient = clientRepository.save(client);
        return new ResponseEntity<>(new ClientDTO(savedClient), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client not found with id " + id, HttpStatus.NOT_FOUND);
        }

        if (clientDTO.getFirstName() != null && !clientDTO.getFirstName().isBlank()) {
            client.setFirstName(clientDTO.getFirstName());
        }
        if (clientDTO.getLastName() != null && !clientDTO.getLastName().isBlank()) {
            client.setLastName(clientDTO.getLastName());
        }
        if (clientDTO.getEmail() != null && !clientDTO.getEmail().isBlank()) {
            client.setEmail(clientDTO.getEmail());
        }

        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<>(new ClientDTO(updatedClient), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateClient(
            @PathVariable Long id,
            @RequestBody ClientDTO clientDTO) {

        Client client = clientRepository.findById(id).orElse(null);

        if (client == null) {
            return new ResponseEntity<>("Client not found with id " + id, HttpStatus.NOT_FOUND);
        }

        if (clientDTO.getFirstName() != null && !clientDTO.getFirstName().isBlank()) {
            client.setFirstName(clientDTO.getFirstName());
        }
        if (clientDTO.getLastName() != null && !clientDTO.getLastName().isBlank()) {
            client.setLastName(clientDTO.getLastName());
        }
        if (clientDTO.getEmail() != null && !clientDTO.getEmail().isBlank()) {
            client.setEmail(clientDTO.getEmail());
        }

        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<>(new ClientDTO(updatedClient), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        if (!clientRepository.existsById(id)) {
            return new ResponseEntity<>("Client not found with id " + id, HttpStatus.NOT_FOUND);
        }

        clientRepository.deleteById(id);
        return new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
    }

    private String hashPassword(String password) {
        // Implementa tu lógica de hash aquí
        return password; // Solo para propósitos de demostración
    }
}