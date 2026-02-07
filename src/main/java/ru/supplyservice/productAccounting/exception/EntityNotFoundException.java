package ru.supplyservice.productAccounting.exception;

public class EntityNotFoundException extends ProductAccountingException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
