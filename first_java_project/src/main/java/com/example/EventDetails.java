package com.example;

// This class represents the details of an event, including its name, date, and location.
// It provides a constructor to initialize these fields and getter methods to retrieve their values.

import java.util.Date;

public class EventDetails {
    private String name;
    private Date date;
    private String location;

    public EventDetails(String name, Date date, String location) {
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
