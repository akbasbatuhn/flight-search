package com.amadeus.flightsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Airport Request",
        description = "Schema to hold Airport create request details"
)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportRequestDTO {

    @Schema(
            description = "City",
            example = "Ankara"
    )
    @NotEmpty(message = "City name can not be a null or empty")
    @Size(min = 3, max = 15, message = "City name length can be minimum 3, maximum 15")
    private String city;
}
