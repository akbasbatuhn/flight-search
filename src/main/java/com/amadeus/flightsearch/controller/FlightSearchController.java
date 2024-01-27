package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.dto.FlightSearchDTO;
import com.amadeus.flightsearch.service.FlightSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequestMapping("/api/v1/search-flight")
@RestController
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @GetMapping("/exact")
    public ResponseEntity<List<FlightSearchDTO>> searchExactFlights(
            @RequestParam Long departureAirportId,
            @RequestParam Long destinationAirportId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime returnDate) {
        List<FlightSearchDTO> flightDTOs = flightSearchService.getExactFlights(
                departureAirportId,
                destinationAirportId,
                departureDate,
                returnDate);

        return ResponseEntity.status(HttpStatus.OK).body(flightDTOs);
    }

    @GetMapping
    public ResponseEntity<List<FlightSearchDTO>> searchAvailableFlights(
            @RequestParam Long departureAirportId,
            @RequestParam Long destinationAirportId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime returnDate) {
        List<FlightSearchDTO> flightDTOs = flightSearchService.getAvailableFlights(
                departureAirportId,
                destinationAirportId,
                departureDate,
                returnDate);

        return ResponseEntity.status(HttpStatus.OK).body(flightDTOs);
    }

}
