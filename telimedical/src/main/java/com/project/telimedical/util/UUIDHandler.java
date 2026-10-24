package com.project.telimedical.util;

import java.util.UUID;

public class UUIDHandler {

    public static UUID convertToUUID(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("UUID cannot be null or blank");
        }

        String normalized = id.trim();

        // Support standard UUID format directly.
        try {
            return UUID.fromString(normalized);
        } catch (IllegalArgumentException ignored) {
            // Fall through to compact hex conversion.
        }

        if (normalized.startsWith("0x") || normalized.startsWith("0X")) {
            normalized = normalized.substring(2); // remove 0x
        }
        normalized = normalized.toLowerCase();

        if (normalized.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID format: " + id);
        }

        String formatted = normalized.substring(0, 8) + "-" +
                       normalized.substring(8, 12) + "-" +
                       normalized.substring(12, 16) + "-" +
                       normalized.substring(16, 20) + "-" +
                       normalized.substring(20);
        return UUID.fromString(formatted);
    }
}