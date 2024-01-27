package com.amadeus.flightsearch.service;

import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.entity.Flight;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class MockFlightDataService {

    private final List<String> cities = Arrays.asList("Muğla", "Adana", "Gaziantep", "İzmir", "İstanbul");
    private final int MOCK_DATA_AMOUNT = 5;
    Random random = new Random();

    public List<Flight> getMockFlightData() {
        List<Flight> flights = new ArrayList<>();

        for (int i = 1; i <= MOCK_DATA_AMOUNT; i++) {
            Airport departureAirport = Airport.builder()
                    .city(cities.get((i + 1) % cities.size())).build();

            Airport destinationAirport = Airport.builder()
                    .city(cities.get((i + 2) % cities.size())).build();

            LocalDateTime now = LocalDateTime.now().withNano(0);

            LocalDateTime departureDateTime =
                    now.plusDays(random.nextInt(12)).plusHours(random.nextInt(24)).plusMinutes(random.nextInt(60));
            LocalDateTime returnDateTime =
                    departureDateTime.plusDays(random.nextInt(5)).plusHours(random.nextInt(24)).plusMinutes(random.nextInt(60));

            BigDecimal price = BigDecimal.valueOf(random.nextInt(700) + (long) random.nextInt(1500));

            Flight flight = Flight.builder()
                    .id((long) i)
                    .price(price)
                    .departureAirport(departureAirport)
                    .destinationAirport(destinationAirport)
                    .departureDate(departureDateTime)
                    .returnDate(returnDateTime).build();

            flights.add(flight);
        }

        return flights;
    }
}
