package com.amadeus.flightsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequestDTO {
    private Long departureAirportId;
    private Long destinationAirportId;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private BigDecimal price;
}
