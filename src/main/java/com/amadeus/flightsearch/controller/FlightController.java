package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.dto.ErrorResponseDTO;
import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.dto.FlightRequestDTO;
import com.amadeus.flightsearch.dto.ResponseDTO;
import com.amadeus.flightsearch.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@Tag(
        name = "CRUD REST APIs for Flights",
        description = "CRUD REST APIs in Flight to CREATE, UPDATE, GET and DELETE flight details"
)
@Validated
@RequestMapping("/api/v1/flights")
@RestController
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(
            summary = "Create Flight",
            description = "REST API to create new Flight"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<FlightDTO> createFlight(@Valid @RequestBody FlightRequestDTO flightRequestDTO) {
        FlightDTO flightDTO = flightService.createFlight(flightRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(flightDTO);
    }

    @Operation(
            summary = "Fetch All Flights",
            description = "Fetch all Flights"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        List<FlightDTO> flightDTOList = flightService.getAllFlights();
        return ResponseEntity.status(HttpStatus.OK).body(flightDTOList);
    }

    @Operation(
            summary = "Fetch All Flight Details (Pagination)",
            description = "Fetch all Flight details pagination version"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<FlightDTO>> getAllFlightsPaged(@RequestParam int pageNumber, @RequestParam int pageSize) {
        Page<FlightDTO> flightDTOs = flightService.getAllFlightsPaged(pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(flightDTOs);
    }

    @Operation(
            summary = "Fetch Flight Details with id",
            description = "Fetch Flight details based on flight id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> createFlight(@PathVariable Long id) {
        FlightDTO flightDTO = flightService.fetchFlightById(id);
        return ResponseEntity.status(HttpStatus.OK).body(flightDTO);
    }

    @Operation(
            summary = "Update Flight details",
            description = "Update Flight details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id, @Valid @RequestBody FlightRequestDTO flightRequestDTO) {
        FlightDTO flightDTO = flightService.updateFlight(id, flightRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(flightDTO);
    }

    @Operation(
            summary = "Delete Flight",
            description = "Delete Flight details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlightById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO("200", "Flight successfully deleted"));
    }
}
