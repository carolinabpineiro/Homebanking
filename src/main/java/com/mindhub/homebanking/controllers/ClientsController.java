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

import static java.util.stream.Collectors.toList;

@RestController //Escucha peticiones especificas
@RequestMapping("/api/clients") //Define ruta de acceso a este controlador

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
        return clientRepository.findAll()//lista de todos los clientes en la base de datos
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    //crud crear cliente
    @PostMapping("/create")  //tipo post para crear un cliente
    public Client createClient(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {  //request param le dice a spring que solicito ese parametro
        return clientRepository.save(new Client(firstName, lastName, email)); //validar q no sean string vacios, if first name.isBlank() return false
    }

    //crud para actualizar un cliente
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return new ResponseEntity<>("Client not found with id " + id, HttpStatus.NOT_FOUND);
        }
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);

        Client updatedClient = clientRepository.save(client);  //sobreescribo el cliente que ya tenia
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdateClient(
            @PathVariable Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email) {

        Client client = clientRepository.findById(id).orElse(null);

        if (client == null) {
            return new ResponseEntity<>("Client not found with id " + id, HttpStatus.NOT_FOUND);
        }

        if (firstName != null) {
            client.setFirstName(firstName);
        }
        if (lastName != null) {
            client.setLastName(lastName);
        }
        if (email != null) {
            client.setEmail(email);
        }

        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }


    //crud para borrar un cliente
    @DeleteMapping("/delete/{id}")
    public ResponseEntity <String> deleteClient(@PathVariable Long id) {
        Client client = clientRepository.findById(id).orElse(null);

        if (client==null){
            return new ResponseEntity<>("Client not found with id " + id, HttpStatus.NOT_FOUND);
        }

        clientRepository.save(client);

        return new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
    }

}
