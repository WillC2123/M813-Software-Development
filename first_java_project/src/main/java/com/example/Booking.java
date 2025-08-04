package com.example;

// This class represents a booking for an event, including details about the event, equipment, DJs, 
// and the user making the booking.

import java.util.List;

public class Booking {
    private String bookingId;
    private EventDetails event;
    private List<Equipment> equipmentList;
    private List<DJ> djList;
    private User user;

    public Booking(String bookingId, EventDetails event, List<Equipment> equipmentList, List<DJ> djList, User user) {
        this.bookingId = bookingId;
        this.event = event;
        this.equipmentList = equipmentList;
        this.djList = djList;
        this.user = user;
    }

    public String getBookingId() {
        return bookingId;
    }

    public EventDetails getEvent() {
        return event;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public List<DJ> getDjList() {
        return djList;
    }

    public User getUser() {
        return user;
    }
}
