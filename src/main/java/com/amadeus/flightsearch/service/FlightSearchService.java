package com.amadeus.flightsearch.service;

import com.amadeus.flightsearch.dto.FlightSearchDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightSearchService {

    private final FlightService flightService;

    public FlightSearchService(FlightService flightService) {
        this.flightService = flightService;
    }

    public List<FlightSearchDTO> getExactFlights(Long departureAirportId,
                                                 Long destinationAirportId,
                                                 LocalDateTime departureDate,
                                                 LocalDateTime returnDate) {
        return flightService.findExactFlights(departureAirportId, destinationAirportId, departureDate, returnDate);
    }


    public List<FlightSearchDTO> getAvailableFlights(Long departureAirportId,
                                               Long destinationAirportId,
                                               LocalDateTime departureDate,
                                               LocalDateTime returnDate) {
        return flightService.fetchAvailableFlights(
                departureAirportId, destinationAirportId,
                departureDate, returnDate);
    }
}
