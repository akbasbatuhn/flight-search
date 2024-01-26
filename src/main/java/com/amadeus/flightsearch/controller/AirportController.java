package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.dto.AirportDTO;
import com.amadeus.flightsearch.dto.AirportRequestDTO;
import com.amadeus.flightsearch.dto.ErrorResponseDTO;
import com.amadeus.flightsearch.dto.ResponseDTO;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        name = "CRUD REST APIs for Airport",
        description = "CRUD REST APIs in Airport to CREATE, UPDATE, GET and DELETE airport details"
)
@Validated
@RequestMapping("/api/v1/airports")
@RestController
public class AirportController {

    private final AirportService service;

    public AirportController(AirportService service) {
        this.service = service;
    }

    @Operation(
            summary = "Create Airport",
            description = "REST API to create new Airport"
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
    public ResponseEntity<AirportDTO> createAirport(@Valid @RequestBody AirportRequestDTO airportRequestDTO) {
        AirportDTO airportDTO = service.createAirport(airportRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(airportDTO);
    }

    @Operation(
            summary = "Fetch All Airport Details",
            description = "Fetch all Airport details"
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
    public ResponseEntity<List<AirportDTO>> getAllAirports() {
        List<AirportDTO> airportDTOList = service.getAllAirports();
        return ResponseEntity.status(HttpStatus.OK).body(airportDTOList);
    }

    @Operation(
            summary = "Fetch All Airport Details (Pagination)",
            description = "Fetch all Airport details pagination version"
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
    public ResponseEntity<Page<Airport>> getAllAirportsPaged(@RequestParam int pageNumber, @RequestParam int pageSize) {
        Page<Airport> airports = service.getAllAirportsPaged(pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(airports);
    }

    @Operation(
            summary = "Fetch Airport Details with id",
            description = "Fetch Airport details based on airport id"
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
    public ResponseEntity<AirportDTO> getAirportById(@PathVariable Long id) {
        AirportDTO airportDTO = service.fetchAirportById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(airportDTO);
    }

    @Operation(
            summary = "Update Airport details(City)",
            description = "Update Airport details"
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
    public ResponseEntity<AirportDTO> updateAirportDetails(@PathVariable Long id, @Valid @RequestBody AirportRequestDTO airportRequestDTO) {
        AirportDTO airportDTO = service.updateAirportDetails(id, airportRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(airportDTO);
    }

    @Operation(
            summary = "Delete Airport",
            description = "Delete Airport details"
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
    public ResponseEntity<ResponseDTO> deleteAirportById(@PathVariable Long id) {
        service.deleteAirportById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("200", "Success"));
    }
}
