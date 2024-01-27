package com.amadeus.flightsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        name = "Flight Search",
        description = "Schema to hold Flight Search details"
)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightSearchDTO {

    @Schema(
            description = "Price",
            example = "1907"
    )
    @NotNull(message = "Price can not be a null or empty")
    @DecimalMin(value = "1", message = "Price should be greater than 0")
    private BigDecimal price;

    @Schema(
            description = "Depature Airport",
            example = "İzmir"
    )
    @NotEmpty(message = "City name can not be a null or empty")
    private String departureAirport;

    @Schema(
            description = "Destination Airport",
            example = "İstanbul"
    )
    @NotEmpty(message = "City name can not be a null or empty")
    private String destinationAirport;

    @Schema(
            description = "Depature Date",
            example = "2024-11-11T20:40:30"
    )
    @NotNull(message = "Price can not be a null or empty")
    private LocalDateTime departureDate;
}
