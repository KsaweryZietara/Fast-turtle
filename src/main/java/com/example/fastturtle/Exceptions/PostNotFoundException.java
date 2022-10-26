package com.example.fastturtle.Exceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(Long id) {
        super("Could not find the post " + id);
    }
}
