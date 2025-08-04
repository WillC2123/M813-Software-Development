package com.example;

// This class manages a collection of users, allowing for retrieval by userID and the addition of new users.
// It uses a HashMap to store users, providing efficient access and modification capabilities.

import java.util.*;

public class UserRepository {
    private Map<String, User> users = new HashMap<>();

    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
}