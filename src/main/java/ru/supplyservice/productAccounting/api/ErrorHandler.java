package ru.supplyservice.productAccounting.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.supplyservice.productAccounting.exception.FruitAccountingException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(FruitAccountingException.class)
    public ResponseEntity<String> handleNotFoundUserException(FruitAccountingException error){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
    }
}
