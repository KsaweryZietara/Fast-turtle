package com.example.fastturtle.Exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("Could not find the comment " + id);
    }
}
