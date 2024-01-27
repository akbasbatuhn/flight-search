package com.amadeus.flightsearch.repository;

import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureAirportAndDestinationAirportAndDepartureDate(
            Airport departureAirport, Airport destinationAirport, LocalDateTime departureDate);

    List<Flight> findByDepartureAirportAndDestinationAirportAndDepartureDateBetweenAndReturnDateNull(
            Airport departureAirport, Airport destinationAirport, LocalDateTime startDate, LocalDateTime endDate
    );

    List<Flight> findByDepartureAirportAndDestinationAirportAndDepartureDateBetweenAndDepartureDateIsNotNullAndReturnDateBetweenAndReturnDateIsNotNull(
            Airport departureAirport, Airport destinationAirport,
            LocalDateTime departureStartDate, LocalDateTime departureEndDate,
            LocalDateTime returnStartDate, LocalDateTime returnEndDate
    );
}
