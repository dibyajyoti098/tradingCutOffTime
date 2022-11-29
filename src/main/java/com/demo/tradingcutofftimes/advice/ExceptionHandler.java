package com.demo.tradingcutofftimes.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public Set<String> handleInvalidArgument(ConstraintViolationException ex){

        Set<String> errorSet=new HashSet<>();
        ex.getConstraintViolations().forEach(error->{
            errorSet.add(error.getMessage());
      });
        return errorSet;
    }
}
