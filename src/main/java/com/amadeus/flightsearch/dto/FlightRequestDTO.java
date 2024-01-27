package com.amadeus.flightsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        name = "Flight Request",
        description = "Schema to hold Flight create & update requests details"
)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequestDTO {

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
    @NotNull(message = "City name can not be a null or empty")
    @Min(value = 1, message = "Airport id can be minimum 1")
    private Long departureAirportId;

    @Schema(
            description = "Destination Airport",
            example = "İstanbul"
    )
    @NotNull(message = "City name can not be a null or empty")
    @Min(value = 1, message = "Airport id can be minimum 1")
    private Long destinationAirportId;

    @Schema(
            description = "Depature Date",
            example = "2024-11-11T20:40:30"
    )
    @NotNull(message = "Departure date can not be a null or empty")
    private LocalDateTime departureDate;

    @Schema(
            description = "Depature Date",
            example = "2024-11-11T20:40:30"
    )
    @Nullable
    private LocalDateTime returnDate;
}
