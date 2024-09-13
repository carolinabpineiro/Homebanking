package com.Mindhubcohort55.Homebanking.dtos;

//RECORD es una forma consisa de generar una clase inmutable q tiene un conjunto fijo de campos

public record RegisterDTO (String firstName, String lastName, String email, String password) {

}
