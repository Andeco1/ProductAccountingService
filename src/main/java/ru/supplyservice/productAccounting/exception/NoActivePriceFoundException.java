package ru.supplyservice.productAccounting.exception;

public class NoActivePriceFoundException extends EntityNotFoundException {
  public NoActivePriceFoundException(String message) {
    super(message);
  }
}
