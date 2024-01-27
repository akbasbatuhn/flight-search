package com.amadeus.flightsearch.service;

import com.amadeus.flightsearch.TestSupport;
import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.dto.FlightRequestDTO;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.entity.Flight;
import com.amadeus.flightsearch.exception.ResourceNotFoundException;
import com.amadeus.flightsearch.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@SpringBootTest
class FlightServiceTest extends TestSupport {

    private FlightService flightService;
    private AirportService airportService;
    private FlightRepository flightRepository;

    Airport departureAirport;
    Airport destinationAirport;
    Flight oneWayFlight;
    Flight roundTripFlight;
    FlightRequestDTO oneWayFlightRequestDTO;
    FlightRequestDTO roundTripFlightRequestDTO;

    FlightRequestDTO invalidDateFlightRequestDTO;
    FlightDTO oneWayFlightDTO;
    FlightDTO roundTripFlightDTO;

    @BeforeEach
    void setUp() {
        destinationAirport = generateDestinationAirport();
        departureAirport = generateDepartureAirport();

        oneWayFlight = generateOneWayFlight(departureAirport, destinationAirport);
        roundTripFlight = generateRoundTripFlight(departureAirport, destinationAirport);

        oneWayFlightRequestDTO = generateOneWayFlightRequestDTO(departureAirport, destinationAirport);
        roundTripFlightRequestDTO = generateRoundTripFlightRequestDTO(departureAirport, destinationAirport);
        invalidDateFlightRequestDTO = generateInvalidDateFlightDTO(departureAirport, destinationAirport);

        oneWayFlightDTO = generateOneWayFlightDTO(departureAirport, destinationAirport);
        roundTripFlightDTO = generateRoundTripFlightDTO(departureAirport, destinationAirport);

        flightRepository = mock(FlightRepository.class);
        airportService = mock(AirportService.class);
        flightService = new FlightService(flightRepository, airportService);
    }

    @Test
    void testCreateFlight_whenRoundTripFlightGiven_shouldReturnFlightDTO() {
        when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(roundTripFlight);

        FlightDTO result = flightService.createFlight(roundTripFlightRequestDTO);

        assertEquals(result.getPrice(), roundTripFlight.getPrice());
        assertEquals(result.getDepartureAirport(), roundTripFlight.getDepartureAirport().getCity());
        assertEquals(result.getDestinationAirport(), roundTripFlight.getDestinationAirport().getCity());
        assertEquals(result.getReturnDate(), roundTripFlight.getReturnDate());
        assertEquals(result.getDepartureDate(), roundTripFlight.getDepartureDate());
    }

    @Test
    void testCreateFlight_whenReturnDateNotExist_shouldReturnFlightDTO() {
        when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(oneWayFlight);

        FlightDTO result = flightService.createFlight(oneWayFlightRequestDTO);

        assertEquals(result.getPrice(), oneWayFlight.getPrice());
        assertEquals(result.getDepartureAirport(), oneWayFlight.getDepartureAirport().getCity());
        assertEquals(result.getDestinationAirport(), oneWayFlight.getDestinationAirport().getCity());
        assertEquals(result.getDepartureDate(), oneWayFlight.getDepartureDate());
        assertNull(result.getReturnDate());
    }

    @Test
    void testReturnDateGreater_whenDepartureDateGreater_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            flightService.checkReturnDateGreater(invalidDateFlightRequestDTO);
        });

        String expectedMessage = "Return date should be later than departure date";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetAllAirport_whenAirportsExist_shouldReturnListOfAirportDTOs() {
        when(flightRepository.findAll()).thenReturn(Arrays.asList(oneWayFlight, roundTripFlight));

        List<FlightDTO> flightDTOList = flightService.getAllFlights();

        assertNotEquals(0, flightDTOList.size());
        assertEquals(2, flightDTOList.size());
        assertEquals(BigDecimal.valueOf(1907), flightDTOList.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(5000), flightDTOList.get(1).getPrice());
    }

    @Test
    void testGetAllAirport_whenAirportExist_shouldReturnPagesOfAirportDTOs() {
        Page<Flight> flights = mock(Page.class);

        when(flightRepository.findAll(Mockito.any(Pageable.class))).thenReturn(flights);

        Page<FlightDTO> flightPagedList = flightService.getAllFlightsPaged(0, 5);

        assertNotNull(flightPagedList);
    }

    @Test
    void testGetAirportById_whenAirportIdExist_shouldReturnAirportDTO() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(oneWayFlight));

        FlightDTO flightDTO = flightService.fetchFlightById(1L);

        assertEquals(BigDecimal.valueOf(1907), flightDTO.getPrice());
    }

    @Test
    void testGetAirportById_whenAirportIdNotExist_shouldThrowResourceNotFoundException() {
        when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(roundTripFlight);
        when(flightRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);

        FlightDTO flightDTO = flightService.createFlight(roundTripFlightRequestDTO);

        assertEquals(BigDecimal.valueOf(5000), flightDTO.getPrice());
        assertThrows(ResourceNotFoundException.class, () -> flightService.fetchFlightById(1L));
    }

    @Test
    void testUpdateRoundFlightById_whenFlightExist_shouldReturnFlightDTO() {
        when(flightRepository.findById(2L)).thenReturn(Optional.of(roundTripFlight));
        when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(roundTripFlight);
        when(airportService.findAirportById(1L)).thenReturn(departureAirport);
        when(airportService.findAirportById(2L)).thenReturn(destinationAirport);

        FlightDTO result = flightService.updateFlight(2L, roundTripFlightRequestDTO);

        assertEquals(roundTripFlight.getPrice(), result.getPrice());
        assertEquals(roundTripFlight.getDepartureAirport().getCity(), result.getDepartureAirport());
        assertEquals(roundTripFlight.getDestinationAirport().getCity(), result.getDestinationAirport());
        assertEquals(roundTripFlight.getReturnDate(), result.getReturnDate());
        assertEquals(roundTripFlight.getDepartureDate(), result.getDepartureDate());
    }

    @Test
    void testDeleteFlightById_whenFlightExist_shouldDeleteFlight() {
        when(flightRepository.findById(roundTripFlight.getId())).thenReturn(Optional.ofNullable(roundTripFlight));

        assertAll(() -> flightService.deleteFlightById(roundTripFlight.getId()));
    }
}