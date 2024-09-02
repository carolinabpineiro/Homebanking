package com.mindhub.homebanking.servicesSecurity;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServicesImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(username);

        if (client == null) {
            throw new UsernameNotFoundException(username);
        }
        if (username.contains("admin")){

        return User .withUsername(username)
                .password(client.getPassword())
                .roles("ADMIN")
                .build();  //esto es como se crea un userdetails spring security
    }
        return User .withUsername(username)
                .password(client.getPassword())
                .roles("CLIENT")
                .build();  //esto es como se crea un userdetails spring security

    }
}
