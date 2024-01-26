package com.amadeus.flightsearch.dto.converter;

import com.amadeus.flightsearch.dto.AirportDTO;
import com.amadeus.flightsearch.entity.Airport;

//@Component
public class AirportDTOConverter {

    private AirportDTOConverter() {}

    public static AirportDTO airportToAirportDTO(Airport from) {
        return AirportDTO.builder()
                .id(from.getId())
                .city(from.getCity()).build();
    }
}
