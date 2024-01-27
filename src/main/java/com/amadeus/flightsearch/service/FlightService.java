package com.amadeus.flightsearch.service;

import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.dto.FlightRequestDTO;
import com.amadeus.flightsearch.dto.converter.FlightDTOConverter;
import com.amadeus.flightsearch.entity.Flight;
import com.amadeus.flightsearch.exception.ResourceNotFoundException;
import com.amadeus.flightsearch.repository.FlightRepository;
import com.amadeus.flightsearch.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportService airportService;

    public FlightService(FlightRepository flightRepository, AirportService airportService) {
        this.flightRepository = flightRepository;
        this.airportService = airportService;
    }

    @Transactional
    public FlightDTO createFlight(FlightRequestDTO flightRequestDTO) {
        checkDatesValid(flightRequestDTO);

        Flight flight = Flight.builder()
                .price(flightRequestDTO.getPrice())
                .destinationAirport(airportService.findAirportById(flightRequestDTO.getDestinationAirportId()))
                .departureAirport(airportService.findAirportById(flightRequestDTO.getDepartureAirportId()))
                .departureDate(flightRequestDTO.getDepartureDate())
                .returnDate(flightRequestDTO.getReturnDate()).build();

        Flight newFlight = flightRepository.save(flight);
        return FlightDTOConverter.flightToFlightDTOConverter(newFlight);
    }

    protected void checkDatesValid(FlightRequestDTO flightRequestDTO) {
        boolean isReturnDateExist = flightRequestDTO.getReturnDate() != null;

        if(isReturnDateExist)
            checkReturnDateGreater(flightRequestDTO);
    }

    protected void checkReturnDateGreater(FlightRequestDTO flightRequestDTO) {
        boolean isReturnDateGreater =
                DateUtil.isReturnDateGreater(flightRequestDTO.getDepartureDate(), flightRequestDTO.getReturnDate());

        if(isReturnDateGreater)
            return;
        throw new IllegalArgumentException("Return date should be later than departure date");
    }

    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll()
                .stream().map(FlightDTOConverter::flightToFlightDTOConverter).toList();
    }

    public Page<FlightDTO> getAllFlightsPaged(int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Flight> flights = flightRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<FlightDTO> flightDTOs =
                flights.getContent().stream().map(FlightDTOConverter::flightToFlightDTOConverter).toList();

        return new PageImpl<>(flightDTOs, pageRequest, flightDTOs.size());
    }

    public FlightDTO fetchFlightById(Long id) {
        Flight flight = findFlightById(id);
        return FlightDTOConverter.flightToFlightDTOConverter(flight);
    }

    protected Flight findFlightById(Long id) {
        return flightRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Flight", "airportId", String.valueOf(id))
        );
    }

    public FlightDTO updateFlight(Long id, FlightRequestDTO flightRequestDTO) {
        Flight flight = findFlightById(id);

        flight.setPrice(flightRequestDTO.getPrice());
        flight.setDepartureDate(flightRequestDTO.getDepartureDate());
        flight.setReturnDate(flightRequestDTO.getReturnDate());
        flight.setDepartureAirport(airportService.findAirportById(flightRequestDTO.getDepartureAirportId()));
        flight.setDestinationAirport(airportService.findAirportById(flightRequestDTO.getDestinationAirportId()));

        Flight newFlight = flightRepository.save(flight);
        return FlightDTOConverter.flightToFlightDTOConverter(newFlight);
    }

    public void deleteFlightById(Long id) {
        findFlightById(id);
        flightRepository.deleteById(id);
    }
}
