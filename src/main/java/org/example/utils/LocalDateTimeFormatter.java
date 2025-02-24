package org.example.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class LocalDateTimeFormatter {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public static LocalDateTime format(String date) {
        return LocalDateTime.parse(date, FORMATTER);
    }
}