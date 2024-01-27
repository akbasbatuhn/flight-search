package com.amadeus.flightsearch.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private BigDecimal price;
    private String departureAirport;
    private String destinationAirport;
    private LocalDateTime departureDate;

    @Nullable
    private LocalDateTime returnDate;
}
