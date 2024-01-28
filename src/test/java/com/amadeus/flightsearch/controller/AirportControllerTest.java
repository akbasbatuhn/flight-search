package com.amadeus.flightsearch.controller;

import com.amadeus.flightsearch.dto.AirportDTO;
import com.amadeus.flightsearch.dto.AirportRequestDTO;
import com.amadeus.flightsearch.dto.converter.AirportDTOConverter;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.service.AirportService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AirportController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @Autowired
    private ObjectMapper objectMapper;

    Airport airport1;
    Airport airport2;
    AirportRequestDTO airportRequestDTO;
    AirportRequestDTO airportUpdateRequestDTO;
    AirportDTO airportDTO;

    @BeforeEach
    void setUp() {
        airport1 = Airport.builder().id(1L).city("İzmir").build();
        airport2 = Airport.builder().id(2L).city("Ankara").build();
        airportRequestDTO = AirportRequestDTO.builder().city("İzmir").build();
        airportUpdateRequestDTO = AirportRequestDTO.builder().city("Ankara").build();
        airportDTO = AirportDTO.builder().id(1L).city("İzmir").build();
    }

    @Test
    void AirportController_CreateAirport_ReturnCreatedAirport() throws Exception {
        when(airportService.createAirport(Mockito.any(AirportRequestDTO.class))).thenReturn(AirportDTOConverter.airportToAirportDTO(airport1));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airportRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(airport1.getCity()));
    }

    @Test
    void AirportController_GetAllAirports_ReturnAirportList() throws Exception {
        List<Airport> airports = new ArrayList<>();

        when(airportService.getAllAirports()).thenReturn(airports.stream().map(AirportDTOConverter::airportToAirportDTO).toList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airports"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(airports.size()));
    }

    @Test
    void AirportController_GetAirportById_ReturnAirport() throws Exception {
        when(airportService.fetchAirportById(1L)).thenReturn(AirportDTOConverter.airportToAirportDTO(airport1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airports/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(airport1.getId()));
    }

    @Test
    void AirportController_UpdateAirport_ReturnAirport() throws Exception {
        when(airportService.updateAirportDetails(Mockito.anyLong(), Mockito.any(AirportRequestDTO.class)))
                .thenReturn(AirportDTOConverter.airportToAirportDTO(airport2));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/airports/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airportUpdateRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(airport2.getId()));
    }

    @Test
    void AirportController_DeleteAirportById_DoNothing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/airports/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}