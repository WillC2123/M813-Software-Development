package com.example;

// This class represents a user with a unique ID and a name.
// It provides a constructor to initialize these fields and getter methods to retrieve their values.

public class User {
    private String userId;
    private String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}