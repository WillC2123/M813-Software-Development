package com.example;

// This class represents payment information for a booking.
// It includes fields for the card number, card holder's name, expiry date, and security code (CVV).

public class PaymentInfo {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;

    public PaymentInfo(String cardNumber, String cardHolder, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }
}
