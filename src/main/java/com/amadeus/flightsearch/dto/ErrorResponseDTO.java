package com.amadeus.flightsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@Builder
@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    @Schema(
            description = "API path invoked by client",
            example = "api/v1/airport"
    )
    private String apiPath;
    @Schema(
            description = "Error code representing the error happened",
            example = "404"
    )
    private HttpStatus errorCode;
    @Schema(
            description = "Error message representing the error happened",
            example = "Resource not found"
    )
    private String errorMessage;
    @Schema(
            description = "Time representing when the error happened"
    )
    private LocalDateTime errorTimestamp;
}
