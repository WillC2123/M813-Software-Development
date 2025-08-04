package com.example;

// This class represents a piece of equipment with an ID and a type.
// It provides a constructor to initialize these fields and getter methods to retrieve their values.

public class Equipment {
    private String id;
    private String type;

    public Equipment(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
