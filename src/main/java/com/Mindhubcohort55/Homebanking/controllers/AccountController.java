package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.AccountDto;
import com.Mindhubcohort55.Homebanking.models.Account;
import com.Mindhubcohort55.Homebanking.models.Client;
import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import com.Mindhubcohort55.Homebanking.repositories.ClientRepository;
import com.Mindhubcohort55.Homebanking.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public ResponseEntity<List<AccountDto>> getAllAccounts(){

        List<Account> getAllAccounts = accountRepository.findAll();
        List<AccountDto> allAccountsDto = getAllAccounts.stream().map(AccountDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(allAccountsDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id){

        Optional<Account> accountById = accountRepository.findById(id);
        if(accountById.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            Account accountData = accountById.get();
            AccountDto accountDto = new AccountDto(accountData);
            return new ResponseEntity<>(accountDto,HttpStatus.OK);
        }
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication){

        try{
            Client client = clientRepository.findByEmail(authentication.getName());

            if(client.getAccounts().size() == 3){
                return new ResponseEntity<>("You cannot create a new account at this time. You have reached the maximum number of allowed accounts (3)", HttpStatus.FORBIDDEN);
            }

            Account newAccount = new Account(AccountNumberGenerator.makeAccountNumber(), LocalDateTime.now(), 00.0);
            client.addAccounts(newAccount);
            accountRepository.save(newAccount);
            clientRepository.save(client);

            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Error creating account" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<?> getClientAccounts(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        List<Account> clientAccounts = accountRepository.findByOwner(client);
        List<AccountDto> clientAccountDto = clientAccounts.stream().map(AccountDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(clientAccountDto, HttpStatus.OK );
    }
}
