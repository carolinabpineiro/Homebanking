package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientsController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/hello")
    public String getClients() {
        return "Hello Clients";
    }



    @GetMapping ("/")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<?> createClient(@RequestBody Client newClient) {
        if (newClient == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El cuerpo de la solicitud no puede ser nulo.");
        }

        try {
            // Guardar el nuevo cliente
            Client client = clientRepository.save(newClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el cliente: " + e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedClient.setId(id);
        Client client = clientRepository.save(updatedClient);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
