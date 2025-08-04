package com.example;

// This class represents a custom exception for booking-related errors.
// It extends the Exception class and provides a constructor to initialize the exception message.

public class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}
