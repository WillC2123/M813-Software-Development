package com.example;

// This class represents a DJ with an ID and a name.
// It provides a constructor to initialize these fields and getter methods to retrieve their values.

public class DJ {
    private String id;
    private String name;

    public DJ(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
