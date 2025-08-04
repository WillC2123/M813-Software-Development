package com.example;

// BookingRepository stores each booking, to later be called by ID.

import java.util.*;

public class BookingRepository {
    private Map<String, Booking> bookings = new HashMap<>();

    public boolean existsByEventDetails(EventDetails event) {
        return bookings.values().stream()
                .anyMatch(b -> b.getEvent().getDate().equals(event.getDate())
                             && b.getEvent().getName().equalsIgnoreCase(event.getName()));
    }

    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
    }

    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }
}
