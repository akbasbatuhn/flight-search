package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.dto.AirportDTO;
import com.amadeus.flightsearch.dto.AirportRequestDTO;
import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.dto.FlightRequestDTO;
import com.amadeus.flightsearch.dto.converter.AirportDTOConverter;
import com.amadeus.flightsearch.dto.converter.FlightDTOConverter;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.entity.Flight;
import com.amadeus.flightsearch.service.AirportService;
import com.amadeus.flightsearch.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = FlightController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    Airport airport1;
    Airport airport2;
    Flight flight1;
    Flight flight2;
    FlightRequestDTO flightRequestDTO;
    FlightRequestDTO flightUpdateRequestDTO;
    FlightDTO flightDTO;

    @BeforeEach
    void setUp() {
        airport1 = Airport.builder().id(1L).city("İzmir").build();
        airport2 = Airport.builder().id(2L).city("Ankara").build();
        flight1 = Flight.builder().id(1L).price(BigDecimal.valueOf(1907)).departureDate(LocalDateTime.now())
                .departureAirport(airport1).destinationAirport(airport2)
                .returnDate(LocalDateTime.now().plusDays(2)).build();

        flight2 = Flight.builder().id(2L).price(BigDecimal.valueOf(5000)).departureDate(LocalDateTime.now())
                .departureAirport(airport2).destinationAirport(airport1).build();

        flightRequestDTO = FlightRequestDTO.builder()
                .price(BigDecimal.valueOf(1907)).departureDate(LocalDateTime.now())
                .departureAirportId(1L).destinationAirportId(2L)
                .returnDate(LocalDateTime.now().plusDays(2)).build();

        flightUpdateRequestDTO = FlightRequestDTO.builder()
                .price(BigDecimal.valueOf(5000)).departureDate(LocalDateTime.now())
                .departureAirportId(1L).destinationAirportId(2L)
                .returnDate(LocalDateTime.now().plusDays(2)).build();

        flightDTO = FlightDTO.builder()
                .price(BigDecimal.valueOf(5000)).departureDate(LocalDateTime.now())
                .departureAirport("İzmir").destinationAirport("Ankara")
                .returnDate(LocalDateTime.now().plusDays(2)).build();
    }

    @Test
    void FlightController_CreateFlight_ReturnCreatedFlight() throws Exception {
        when(flightService.createFlight(Mockito.any(FlightRequestDTO.class))).thenReturn(FlightDTOConverter.flightToFlightDTOConverter(flight1));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(flight1.getPrice()));
    }

    @Test
    void FlightController_GetAllFlights_ReturnFlightList() throws Exception {
        List<Flight> flights = new ArrayList<>();

        when(flightService.getAllFlights())
                .thenReturn(flights.stream().map(FlightDTOConverter::flightToFlightDTOConverter).toList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/flights"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(flights.size()));
    }

    @Test
    void FlightController_GetFlightById_ReturnFlight() throws Exception {
        when(flightService.fetchFlightById(1L))
                .thenReturn(FlightDTOConverter.flightToFlightDTOConverter(flight1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/flights/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(flight1.getPrice()));
    }

    @Test
    void FlightController_UpdateFlight_ReturnFlight() throws Exception {
        when(flightService.updateFlight(Mockito.anyLong(), Mockito.any(FlightRequestDTO.class)))
                .thenReturn(FlightDTOConverter.flightToFlightDTOConverter(flight2));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/flights/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightUpdateRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(flight2.getPrice()));
    }

    @Test
    void FlightController_DeleteAirportById_DoNothing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/flights/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}