package com.Mindhubcohort55.Homebanking.utils;

import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountNumberGenerator {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountNumberGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String makeAccountNumber() {
        String leftZero;

        do {
            leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
        } while (accountRepository.existsByAccountNumber("VIN" + leftZero));

        return "VIN" + leftZero;
    }

}