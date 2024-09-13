package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.ClientDTO;
import com.Mindhubcohort55.Homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
    @RequestMapping("/api/clients")
    public class ClientController {

        @Autowired
        private ClientServices clientServices;

        @GetMapping("/")
        public ResponseEntity<List<ClientDTO>> getAllClients() {
            return new ResponseEntity<>( clientServices.getAllClientDTO(), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getClientById(@PathVariable Long id) {
            if (clientServices.getClientById(id) == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }else{
                return new ResponseEntity<>(clientServices.getClientDTO(clientServices.getClientById(id)), HttpStatus.OK);
            }
        }


}
