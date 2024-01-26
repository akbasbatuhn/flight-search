package com.amadeus.flightsearch.service;

import com.amadeus.flightsearch.dto.AirportDTO;
import com.amadeus.flightsearch.dto.AirportRequestDTO;
import com.amadeus.flightsearch.entity.Airport;
import com.amadeus.flightsearch.exception.ResourceAlreadyExistException;
import com.amadeus.flightsearch.exception.ResourceNotFoundException;
import com.amadeus.flightsearch.dto.converter.AirportDTOConverter;
import com.amadeus.flightsearch.repository.AirportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AirportService {

    private final AirportRepository repository;

    public AirportService(AirportRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AirportDTO createAirport(AirportRequestDTO airportRequestDTO) {
        isCityExist(airportRequestDTO.getCity());

        Airport airport = Airport.builder()
                .city(airportRequestDTO.getCity()).build();

        Airport newAirport = repository.save(airport);

        return AirportDTOConverter.airportToAirportDTO(newAirport);
    }

    protected void isCityExist(String city) {
        Optional<Airport> airport = repository.findByCity(city);
        if(airport.isPresent())
            throw new ResourceAlreadyExistException("Airport", "City Name", city);
    }

    public List<AirportDTO> getAllAirports() {
        List<Airport> airports = repository.findAll();
        return airports.stream().map(AirportDTOConverter::airportToAirportDTO).collect(Collectors.toList());
    }

    public Page<Airport> getAllAirportsPaged(int pageNumber, int pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public AirportDTO fetchAirportById(Long id) {
        Airport airport = findAirportById(id);
        return AirportDTOConverter.airportToAirportDTO(airport);
    }

    protected Airport findAirportById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Airport", "airportId", String.valueOf(id))
        );
    }

    @Transactional
    public AirportDTO updateAirportDetails(Long airportId, AirportRequestDTO airportRequestDTO) {
        Airport airport = findAirportById(airportId);
        airport.setCity(airportRequestDTO.getCity());
        repository.save(airport);

        return AirportDTOConverter.airportToAirportDTO(airport);
    }

    @Transactional
    public void deleteAirportById(Long id) {
        findAirportById(id);
        repository.deleteById(id);
    }
}
