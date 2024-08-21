package com.example.ticket_booking.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    /**
     * Convert a string to LocalDateTime using the predefined format.
     * @param dateTimeString the date-time string to parse
     * @return the LocalDateTime object
     * @throws DateTimeParseException if the date-time string is not in the expected format
     */
    public static LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Convert LocalDateTime to a string using the predefined format.
     * @param dateTime the LocalDateTime object to format
     * @return the formatted date-time string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
