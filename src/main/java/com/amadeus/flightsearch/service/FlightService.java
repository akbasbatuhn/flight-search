package com.amadeus.flightsearch.service;

import com.amadeus.flightsearch.dto.FlightDTO;
import com.amadeus.flightsearch.dto.FlightRequestDTO;
import com.amadeus.flightsearch.dto.FlightSearchDTO;
import com.amadeus.flightsearch.dto.converter.FlightDTOConverter;
import com.amadeus.flightsearch.entity.Airport;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<FlightSearchDTO> findExactFlights(
                                    Long departureAirportId,
                                    Long destinationAirportId,
                                    LocalDateTime departureDate,
                                    LocalDateTime returnDate) {
        Airport departureAirport = airportService.findAirportById(departureAirportId);
        Airport destinationAirport = airportService.findAirportById(destinationAirportId);

        List<Flight> flights;

        if(returnDate == null) {
            flights = flightRepository.findByDepartureAirportAndDestinationAirportAndDepartureDate(
                            departureAirport, destinationAirport, departureDate
                    );
        }
        else {
            flights = flightRepository.findByDepartureAirportAndDestinationAirportAndDepartureDate(
                    departureAirport, destinationAirport, departureDate
            );

            // Returning flights with given returnDate
            // This time destinationAirport --> departureAirport
            List<Flight> otherWay = flightRepository.findByDepartureAirportAndDestinationAirportAndDepartureDate(
                    destinationAirport, departureAirport, returnDate
            );

            flights.addAll(otherWay);
        }

        if(flights.isEmpty())
            throw new ResourceNotFoundException("Flight not found with given date");

        return flights.stream().map(FlightDTOConverter::flightToFlightSearchDTOConverter).toList();
    }

    // This method includes flights that
    // in same day AND before
    // Ex: if DepartureDate is 2024-11-11T20:00:00
    // This method finds all flights between 2024-11-11T00:00:01 to 2024-11-11T20:00:00
    public List<FlightSearchDTO> fetchAvailableFlights(
            Long departureAirportId,
            Long destinationAirportId,
            LocalDateTime departureDate,
            LocalDateTime returnDate) {

        Airport departureAirport = airportService.findAirportById(departureAirportId);
        Airport destinationAirport = airportService.findAirportById(destinationAirportId);

        List<Flight> flights = findAvailableOneWayFlights(departureAirport, destinationAirport, departureDate);

        if(returnDate != null) {
            flights.addAll(findAvailableOneWayFlights(destinationAirport, departureAirport, returnDate));
            flights.addAll(findAvailableRoundTripFlights(departureAirport, destinationAirport, departureDate, returnDate));
        }

        if(flights.isEmpty())
            throw new ResourceNotFoundException("Flight not found with given data");

        return flights.stream().map(FlightDTOConverter::flightToFlightSearchDTOConverter).toList();
    }

    protected List<Flight> findAvailableOneWayFlights(
            Airport departureAirport,
            Airport destinationAirport,
            LocalDateTime departureDate) {
        LocalDate targetDate = departureDate.toLocalDate();
        LocalTime targetTime = departureDate.toLocalTime();

        // Start of that day
        LocalDateTime departureStartDateTime = LocalDateTime.of(targetDate, LocalTime.MIN);
        LocalDateTime departureEndDateTime = LocalDateTime.of(targetDate, targetTime);

        return flightRepository.findByDepartureAirportAndDestinationAirportAndDepartureDateBetweenAndReturnDateNull(
                        departureAirport, destinationAirport, departureStartDateTime, departureEndDateTime);
    }

    protected List<Flight> findAvailableRoundTripFlights(
            Airport departureAirport,
            Airport destinationAirport,
            LocalDateTime departureDate,
            LocalDateTime returnDate) {
        List<Flight> result = new ArrayList<>();
        LocalDate targetDate = departureDate.toLocalDate();
        LocalTime targetTime = departureDate.toLocalTime();

        // Start of day
        LocalDateTime departureStartDateTime = LocalDateTime.of(targetDate, LocalTime.MIN);
        LocalDateTime departureEndDateTime = LocalDateTime.of(targetDate, targetTime);
        System.out.println(departureStartDateTime + " departure start time");
        System.out.println(departureEndDateTime);

        targetDate = returnDate.toLocalDate();
        targetTime = returnDate.toLocalTime();

        // Start of day
        LocalDateTime destinationStartDateTime = LocalDateTime.of(targetDate, LocalTime.MIN);
        LocalDateTime destinationEndDateTime = LocalDateTime.of(targetDate, targetTime);
        System.out.println(destinationStartDateTime + " destination start time");
        System.out.println(departureEndDateTime);

        List<Flight> departureResultList =
                flightRepository.findByDepartureAirportAndDestinationAirportAndDepartureDateBetweenAndDepartureDateIsNotNullAndReturnDateBetweenAndReturnDateIsNotNull(
                        departureAirport, destinationAirport, departureStartDateTime, departureEndDateTime, destinationStartDateTime, destinationEndDateTime);

        if(!departureResultList.isEmpty()) {
            for(Flight flight: departureResultList) {
                result.add(flight);

                Flight second = Flight.builder()
                        .id(flight.getId())
                        .price(flight.getPrice())
                        .departureAirport(flight.getDestinationAirport())
                        .destinationAirport(flight.getDepartureAirport())
                        .departureDate(flight.getReturnDate())
                        .returnDate(null).build();

                result.add(second);
            }
        }
        /*
        List<Flight> destinationResultList =
                flightRepository.findByDepartureAirportAndDestinationAirportAndDepartureDateBetweenAndReturnDateBetween(
                        destinationAirport, departureAirport, destinationStartDateTime, destinationEndDateTime, destinationStartDateTime, destinationEndDateTime);

        departureResultList.addAll(destinationResultList);*/
        return result;
    }
}
