package com.example;

// This class provides a method to generate unique IDs with a specified prefix, using UUIDs.

import java.util.UUID;

public class IdGenerator {
    public static String generateUniqueId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString();
    }
}