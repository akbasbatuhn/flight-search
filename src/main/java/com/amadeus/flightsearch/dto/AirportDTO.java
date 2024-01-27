package com.amadeus.flightsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Airport",
        description = "Schema to hold Airport details"
)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportDTO {

    @Schema(
            description = "Airport id",
            example = "11111111"
    )
    @NotEmpty(message = "Account number can not be a null or empty")
    private Long id;

    @Schema(
            description = "City",
            example = "Ankara"
    )
    @NotEmpty(message = "City name can not be a null or empty")
    @Size(min = 3, max = 15, message = "City name length can be minimum 3, maximum 15")
    private String city;
}
