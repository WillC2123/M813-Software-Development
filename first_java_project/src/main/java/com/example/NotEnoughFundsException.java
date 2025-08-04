package com.example;

/**
 * NotEnoughFundsException is thrown when a booking cannot be processed due to insufficient funds.
 * It extends the Exception class to provide a custom exception for this specific scenario.
 */

public class NotEnoughFundsException extends Exception {
    public NotEnoughFundsException(String message) {
        super(message);
    }
}