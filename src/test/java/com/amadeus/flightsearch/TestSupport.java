package com.amadeus.flightsearch;

import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.dto.FlightRequestDTO;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.entity.Flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestSupport {

    private final LocalDateTime DEPARTURE_DATE = LocalDateTime.of(2023, 2, 2, 20, 40, 30);
    private final LocalDateTime RETURN_DATE = LocalDateTime.of(2024, 11, 11, 20, 40, 30);

    public Airport generateDepartureAirport() {
        return Airport.builder()
                .id(1L)
                .city("İzmir").build();
    }

    public Airport generateDestinationAirport() {
        return Airport.builder()
                .id(2L)
                .city("Ankara").build();
    }

    public Flight generateOneWayFlight(Airport departureAirport, Airport destinationAirport) {
        return Flight.builder()
                .id(1L)
                .price(BigDecimal.valueOf(1907))
                .departureDate(DEPARTURE_DATE)
                .departureAirport(departureAirport)
                .destinationAirport(destinationAirport)
                .build();
    }

    public Flight generateRoundTripFlight(Airport departureAirport, Airport destinationAirport) {
        return Flight.builder()
                .id(2L)
                .price(BigDecimal.valueOf(5000))
                .departureDate(DEPARTURE_DATE)
                .departureAirport(departureAirport)
                .destinationAirport(destinationAirport)
                .returnDate(RETURN_DATE)
                .build();
    }

    public FlightRequestDTO generateOneWayFlightRequestDTO(Airport departureAirport, Airport destinationAirport) {
        return FlightRequestDTO.builder()
                .price(BigDecimal.valueOf(1907))
                .destinationAirportId(destinationAirport.getId())
                .departureAirportId(departureAirport.getId())
                .departureDate(DEPARTURE_DATE)
                .build();
    }

    public FlightRequestDTO generateRoundTripFlightRequestDTO(Airport departureAirport, Airport destinationAirport) {
        return FlightRequestDTO.builder()
                .price(BigDecimal.valueOf(5000))
                .destinationAirportId(destinationAirport.getId())
                .departureAirportId(departureAirport.getId())
                .departureDate(DEPARTURE_DATE)
                .returnDate(RETURN_DATE)
                .build();
    }

    public FlightDTO generateOneWayFlightDTO(Airport departureAirport, Airport destinationAirport) {
        return FlightDTO.builder()
                .price(BigDecimal.valueOf(1907))
                .destinationAirport(destinationAirport.getCity())
                .departureAirport(departureAirport.getCity())
                .departureDate(DEPARTURE_DATE)
                .build();
    }

    public FlightDTO generateRoundTripFlightDTO(Airport departureAirport, Airport destinationAirport) {
        return FlightDTO.builder()
                        .price(BigDecimal.valueOf(5000))
                        .destinationAirport(destinationAirport.getCity())
                        .departureAirport(departureAirport.getCity())
                        .departureDate(DEPARTURE_DATE)
                        .returnDate(RETURN_DATE)
                        .build();
    }

    public FlightRequestDTO generateInvalidDateFlightDTO(Airport departureAirport, Airport destinationAirport) {
        return FlightRequestDTO.builder()
                .price(BigDecimal.valueOf(5000))
                .destinationAirportId(destinationAirport.getId())
                .departureAirportId(departureAirport.getId())
                .departureDate(RETURN_DATE)
                .returnDate(DEPARTURE_DATE)
                .build();
    }
}
