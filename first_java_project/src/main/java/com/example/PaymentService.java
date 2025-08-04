package com.example;

// This class handles payment processing for bookings.
// It includes methods to process payments and calculate the total amount based on the booking details.

public class PaymentService {

    public boolean processPayment(Booking booking, PaymentInfo paymentInfo) {
        if (booking == null || paymentInfo == null) {
            System.out.println("Payment failed: Missing booking or payment info.");
            return false;
        }

        // Simulate validation and payment processing
        System.out.println("Processing payment for booking: " + booking.getBookingId());
        System.out.println("Charged to: " + paymentInfo.getCardHolder());
        System.out.println("Amount: £" + calculateAmount(booking));  // Flat rate or per asset

        // Simulate success
        return true;
    }

    private int calculateAmount(Booking booking) {
        int base = 100;
        int equipmentCost = booking.getEquipmentList().size() * 50;
        int djCost = booking.getDjList().size() * 100;
        return base + equipmentCost + djCost;
    }
}