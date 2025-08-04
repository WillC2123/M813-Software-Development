package com.example;

public class RefundService {

    /**
     * Processes a refund for a cancelled booking.
     * @param booking The cancelled booking.
     * @param paymentInfo The payment information used for the original booking.
     * @return true if the refund was successful, false otherwise.
     */
    public boolean processRefund(Booking booking, PaymentInfo paymentInfo) {
        if (booking == null || paymentInfo == null) {
            System.out.println("Refund failed: Missing booking or payment info.");
            return false;
        }

        // Simulate refund processing
        System.out.println("Processing refund for booking: " + booking.getBookingId());
        System.out.println("Refunded to: " + paymentInfo.getCardHolder());
        System.out.println("Amount: £" + calculateRefundAmount(booking));

        // Simulate success
        return true;
    }

    private int calculateRefundAmount(Booking booking) {
        int base = 100;
        int equipmentCost = booking.getEquipmentList().size() * 50;
        int djCost = booking.getDjList().size() * 100;
        return base + equipmentCost + djCost;
    }
}
