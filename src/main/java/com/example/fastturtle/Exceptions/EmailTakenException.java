package com.example.fastturtle.Exceptions;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String email) {
        super("Email " + email + " is taken");
    }
}
