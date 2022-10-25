package com.example.fastturtle.Exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("Could not find the user " + id);
    }
}


