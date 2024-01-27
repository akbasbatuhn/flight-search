package com.amadeus.flightsearch.scheduled;

import com.amadeus.flightsearch.entity.Flight;
import com.amadeus.flightsearch.service.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FetchThirdPartyFlightMockData {

    private static Logger logger = LoggerFactory.getLogger(FetchThirdPartyFlightMockData.class);
    private final String ENDPOINT_URL = "http://localhost:8080/api/v1/mock-flight-data";

    private final FlightService flightService;
    private final RestTemplate restTemplate;

    public FetchThirdPartyFlightMockData(FlightService flightService, RestTemplate restTemplate) {
        this.flightService = flightService;
        this.restTemplate = restTemplate;
    }

    // 23:30 for every day
    @Scheduled(cron = "0 30 23 * * *")
    public void getFlightDataFromMockApi() {
        logger.info("Fetch process started");

        List<Flight> response =  restTemplate.exchange(
                ENDPOINT_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Flight>>() {})
                .getBody();

        if(!response.isEmpty())
            flightService.saveAllFlights(response);

        logger.info("Process completed");
    }
}
