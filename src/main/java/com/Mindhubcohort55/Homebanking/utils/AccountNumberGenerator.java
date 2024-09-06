package com.Mindhubcohort55.Homebanking.utils;

import com.Mindhubcohort55.Homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountNumberGenerator {

    @Autowired
    public static AccountRepository accountRepository;
//    private static final Random random = new Random();

    public AccountNumberGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public static String makeAccountNumber(){

        String leftZero;

        do{
            leftZero = String.format("%08d", (int) (Math.random() * (100000000 - 1) + 1));
            //  leftZero = String.format("%08d", random.nextInt(100000000));

        }while (accountRepository.existsByNumber("VIN"+ leftZero));

        return "VIN" + leftZero;
    }
}
