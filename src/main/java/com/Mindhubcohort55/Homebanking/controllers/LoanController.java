package com.Mindhubcohort55.Homebanking.controllers;

import com.Mindhubcohort55.Homebanking.dtos.LoanApplicationDto;
import com.Mindhubcohort55.Homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/")
    public ResponseEntity<?> getLoans() {
        return ResponseEntity.ok(loanService.getLoansDTO());
    }

    @GetMapping("/loansAvailable")
    public ResponseEntity<?> loansAvailable(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(loanService.getLoansDTO());
    }



    @PostMapping("/apply")
    public ResponseEntity<?> applyForLoan(Authentication authentication, @RequestBody LoanApplicationDto loanDTO) {
        String email = authentication.getName();
        return loanService.applyForLoan(email, loanDTO);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}