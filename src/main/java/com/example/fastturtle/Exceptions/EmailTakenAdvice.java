package com.example.fastturtle.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmailTakenAdvice {
    @ResponseBody
    @ExceptionHandler(EmailTakenException.class)
    @ResponseStatus(HttpStatus.OK)
    String emailTakenHandler(EmailTakenException ex){
        return ex.getMessage();
    }
}

