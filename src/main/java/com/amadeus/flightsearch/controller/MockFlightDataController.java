package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.entity.Flight;
import com.amadeus.flightsearch.service.MockFlightDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/mock-flight-data")
@RestController
public class MockFlightDataController {

    private final MockFlightDataService service;

    public MockFlightDataController(MockFlightDataService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getMockFlightData() {
        List<Flight> flightList = service.getMockFlightData();
        return ResponseEntity.status(HttpStatus.OK).body(flightList);
    }
}
