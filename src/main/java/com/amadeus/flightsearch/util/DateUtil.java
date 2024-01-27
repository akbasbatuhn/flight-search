package com.amadeus.flightsearch.util;

import java.time.LocalDateTime;

public class DateUtil {

    private DateUtil() {}

    public static boolean isReturnDateGreater(LocalDateTime departureDate, LocalDateTime returnDate) {
        return returnDate.isAfter(departureDate);
    }
}
