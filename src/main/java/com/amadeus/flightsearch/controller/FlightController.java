package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.dto.FlightRequestDTO;
import com.amadeus.flightsearch.dto.ResponseDTO;
import com.amadeus.flightsearch.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/flights")
@RestController
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightRequestDTO flightRequestDTO) {
        FlightDTO flightDTO = flightService.createFlight(flightRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(flightDTO);
    }

    @GetMapping
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        List<FlightDTO> flightDTOList = flightService.getAllFlights();
        return ResponseEntity.status(HttpStatus.OK).body(flightDTOList);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<FlightDTO>> getAllFlightsPaged(@RequestParam int pageNumber, @RequestParam int pageSize) {
        Page<FlightDTO> flightDTOs = flightService.getAllFlightsPaged(pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(flightDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> createFlight(@PathVariable Long id) {
        FlightDTO flightDTO = flightService.fetchFlightById(id);
        return ResponseEntity.status(HttpStatus.OK).body(flightDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id, @RequestBody FlightRequestDTO flightRequestDTO) {
        FlightDTO flightDTO = flightService.updateFlight(id, flightRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(flightDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlightById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO("200", "Flight successfully deleted"));
    }
}
