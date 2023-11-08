package com.shadril.securityservice.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    private static final String MESSAGE = "User with email %s already exists";
    public UserAlreadyExistException(String email) {
        super(String.format(MESSAGE, email));
    }
}
