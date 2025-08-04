package com.example;

// This class handles sending confirmation emails for bookings.
// It formats the booking details into a message and simulates sending it to the main user and

import java.util.List;

public class EmailService {

    public void sendConfirmationEmail(Booking booking, User user, List<User> stakeholders) {
        if (booking == null || user == null) return;

        String message = String.format(
                "Booking Confirmation:\n- ID: %s\n- Event: %s on %s\n- Location: %s",
                booking.getBookingId(),
                booking.getEvent().getName(),
                booking.getEvent().getDate(),
                booking.getEvent().getLocation()
        );

        // Simulate email to main user
        System.out.println("Sending email to user: " + user.getName());
        System.out.println(message);

        // Simulate stakeholder notifications
        for (User stakeholder : stakeholders) {
            System.out.println("Notifying stakeholder: " + stakeholder.getName());
        }
    }
}
