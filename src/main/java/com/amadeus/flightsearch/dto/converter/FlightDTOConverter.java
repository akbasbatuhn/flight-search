package com.amadeus.flightsearch.dto.converter;

import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.entity.Flight;

public class FlightDTOConverter {

    private FlightDTOConverter() {}

    public static FlightDTO flightToFlightDTOConverter(Flight from) {
        return FlightDTO.builder()
                .price(from.getPrice())
                .departureAirport(from.getDepartureAirport().getCity())
                .destinationAirport(from.getDestinationAirport().getCity())
                .departureDate(from.getDepartureDate())
                .returnDate(from.getReturnDate()).build();
    }
}
