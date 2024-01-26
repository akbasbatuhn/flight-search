package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.dto.AirportDTO;
import com.amadeus.flightsearch.dto.AirportRequestDTO;
import com.amadeus.flightsearch.dto.ResponseDTO;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.service.AirportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/airports")
@RestController
public class AirportController {

    private final AirportService service;

    public AirportController(AirportService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AirportDTO> createFlight(@RequestBody AirportRequestDTO airportRequestDTO) {
        AirportDTO airportDTO = service.createAirport(airportRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(airportDTO);
    }


    @GetMapping
    public ResponseEntity<List<AirportDTO>> getAllAirports() {
        List<AirportDTO> airportDTOList = service.getAllAirports();
        return ResponseEntity.status(HttpStatus.OK).body(airportDTOList);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Airport>> getAllAirportsPaged(@PathVariable int pageNumber, @PathVariable int pageSize) {
        Page<Airport> airports = service.getAllAirportsPaged(pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(airports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportDTO> getAirportById(@PathVariable Long id) {
        AirportDTO airportDTO = service.fetchAirportById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(airportDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportDTO> updateAirportDetails(@PathVariable Long id, @RequestBody AirportRequestDTO airportRequestDTO) {
        AirportDTO airportDTO = service.updateAirportDetails(id, airportRequestDTO);
        return ResponseEntity.status(HttpStatus.FOUND).body(airportDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteAirportById(@PathVariable Long id) {
        service.deleteAirportById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("200", "Success"));
    }
}
