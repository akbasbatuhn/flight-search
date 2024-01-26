package com.amadeus.flightsearch.service;

import com.amadeus.flightsearch.dto.AirportDTO;
import com.amadeus.flightsearch.dto.AirportRequestDTO;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.exception.ResourceAlreadyExistException;
import com.amadeus.flightsearch.exception.ResourceNotFoundException;
import com.amadeus.flightsearch.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class AirportServiceTest {

    private AirportRepository airportRepository;
    private AirportService airportService;

    @BeforeEach
    public void setUp() {
        airportRepository = mock(AirportRepository.class);
        airportService = new AirportService(airportRepository);
    }

    @Test
    void testCreateAirport_whenAirportNotExist_shouldReturnAirportDTO() {
        AirportRequestDTO airportRequestDTO = AirportRequestDTO.builder()
                .city("İzmir").build();

        Airport airport = Airport.builder()
                .city("İzmir")
                .id(35L).build();

        when(airportRepository.save(Mockito.any(Airport.class))).thenReturn(airport);

        AirportDTO savedAirport = airportService.createAirport(airportRequestDTO);

        assertNotNull(savedAirport);
        assertEquals(savedAirport.getCity(), airport.getCity());
    }

    @Test
    void testCreateAirport_whenAirportExist_shouldThrowResourceAlreadyExistException() {
        Airport airport = Airport.builder()
                .city("İzmir")
                .build();

        Airport airportSameCity = Airport.builder()
                .city("İzmir")
                .build();

        AirportRequestDTO sameCityRequest = new AirportRequestDTO("İzmir");

        when(airportRepository.save(airport)).thenReturn(Mockito.any(Airport.class));
        when(airportRepository.save(airportSameCity)).thenThrow(new ResourceAlreadyExistException("Airport", "City Name", "İzmir"));

        assertThrows(ResourceAlreadyExistException.class, () -> airportService.createAirport(sameCityRequest));
    }

    @Test
    void testGetAllAirport_whenAirportsExist_shouldReturnListOfAirportDTOs() {
        Airport airport = new Airport(1L, "İzmir");

        when(airportRepository.findAll()).thenReturn(Arrays.asList(airport));

        List<AirportDTO> airportDTOList = airportService.getAllAirports();

        assertNotEquals(0, airportDTOList.size());
        assertEquals(airportDTOList.get(0).getCity(), airport.getCity());
        assertEquals(airportDTOList.get(0).getId(), airport.getId());
    }

    @Test
    void testGetAllAirport_whenAirportExist_shouldReturnPagesOfAirportDTOs() {
        Page<Airport> airports = mock(Page.class);

        when(airportRepository.findAll(Mockito.any(Pageable.class))).thenReturn(airports);

        Page<Airport> airportPagedList = airportService.getAllAirportsPaged(0, 5);

        assertNotNull(airportPagedList);
    }

    @Test
    void testGetAirportById_whenAirportIdExist_shouldReturnAirportDTO() {
        Airport airport = Airport.builder()
                .city("İzmir")
                .id(5L).build();

        when(airportRepository.findById(airport.getId())).thenReturn(Optional.of(airport));

        AirportDTO airportDTO = airportService.fetchAirportById(airport.getId());

        assertEquals(airportDTO.getId(), airport.getId());
        assertEquals(airportDTO.getCity(), airport.getCity());
    }

    @Test
    void testGetAirportById_whenAirportIdNotExist_shouldThrowResourceNotFoundException() {
        AirportRequestDTO airportRequestDTO = AirportRequestDTO.builder()
                .city("İzmir").build();

        Airport airport = Airport.builder()
                        .city("İzmir")
                        .id(5L).build();

        when(airportRepository.save(Mockito.any(Airport.class))).thenReturn(airport);
        when(airportRepository.findById(10L)).thenThrow(ResourceNotFoundException.class);

        AirportDTO airportDTO = airportService.createAirport(airportRequestDTO);

        assertEquals(airportDTO.getCity(), airport.getCity());
        assertThrows(ResourceNotFoundException.class, () -> airportService.fetchAirportById(10L));
    }

    @Test
    void testExceptionMessage_whenAirportIdNotExist_shouldMessagesSame() {
        when(airportRepository.findById(10L)).thenThrow(new ResourceNotFoundException("Airport", "airportId", "10"));

        Exception ex =
                assertThrows(ResourceNotFoundException.class, () -> airportService.findAirportById(10L));
        assertEquals("Airport not found with input data airportId: '10'", ex.getMessage());
    }

    @Test
    void testUpdateAirportById_whenAirportExist_shouldReturnAirportDTO() {
        AirportRequestDTO airportRequestDTO = AirportRequestDTO.builder()
                .city("Ankara").build();

        Airport airport = Airport.builder()
                .city("İzmir")
                .id(5L).build();

        Airport updatedAirport = Airport.builder()
                .id(5L)
                .city("Ankara").build();

        when(airportRepository.findById(5L)).thenReturn(Optional.of(airport));
        when(airportRepository.save(airport)).thenReturn(updatedAirport);

        AirportDTO airportDTO = airportService.updateAirportDetails(airport.getId(), airportRequestDTO);

        assertEquals(airportDTO.getCity(), airportRequestDTO.getCity());
        assertEquals(airportDTO.getId(), airport.getId());
    }

    @Test
    void testDeleteAirportById_whenAirportExist_shouldDeleteAirport() {
        Airport airport = Airport.builder()
                .city("İzmir")
                .id(35L).build();

        when(airportRepository.findById(35L)).thenReturn(Optional.ofNullable(airport));

        assertAll(() -> airportService.deleteAirportById(35L));
    }

}