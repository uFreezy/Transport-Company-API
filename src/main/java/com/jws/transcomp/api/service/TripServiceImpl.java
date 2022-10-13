package com.jws.transcomp.api.service;

import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.models.dto.trip.TripDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import com.jws.transcomp.api.repository.CompanyRepository;
import com.jws.transcomp.api.repository.TripRepository;
import com.jws.transcomp.api.util.PageRequestUtil;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final CompanyRepository companyRepository;

    public TripServiceImpl(TripRepository tripRepository, CompanyRepository companyRepository) {
        this.tripRepository = tripRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void save(Trip trip) {
        Company company = trip.getCompany();
        for (Client client : trip.getClients()) {
            company.addClient(client);
        }
        company.addClients(trip.getClients());

        this.tripRepository.save(trip);
        this.companyRepository.save(company);
    }

    @Override
    public boolean any() {
        return this.tripRepository.count() > 0;
    }

    @Override
    public boolean delete(Trip trip) {
        if (this.tripRepository.existsById(trip.getId())) {
            this.tripRepository.delete(trip);
            return true;
        }
        return false;
    }

    @Override
    public Trip findById(long id) {
        return this.tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trip id!"));
    }


    @Override
    public List<Trip> getTripsByType(TripType type) {
        Trip exampleTrip = new Trip();
        exampleTrip.setType(type);

        return this.tripRepository.findAll(Example.of(exampleTrip));
    }

    @Override
    public List<Trip> getTripsForDestination(String destName) {
        Trip exampleTrip = new Trip();
        exampleTrip.setEndingPoint(destName);

        return this.tripRepository.findAll(Example.of(exampleTrip));
    }

    @Override
    public List<Trip> getAll() {
        return this.tripRepository.findAll();
    }

    @Override
    public PaginatedResponse filterTrips(Long companyId, String destination, String sortBy, Pageable pageable) {
        if (companyId == null) {
            throw new IllegalArgumentException("Company id is empty.");
        }

        if (sortBy != null) {
            pageable = PageRequestUtil.createPageRequest(pageable, sortBy);
        }

        Page<Trip> trips;
        if (destination != null) {
            trips = this.tripRepository.findAllByCompanyIdAndEndingPoint(companyId, destination, pageable);
        } else {
            trips = this.tripRepository.findAllByCompanyId(companyId, pageable);
        }

        return new PaginatedResponse(PaginatedResponse.mapDto(trips.getContent(), TripDto.class),
                trips.getTotalElements(),
                trips.getTotalPages());
    }

    @Override
    public long count() {
        return this.tripRepository.count();
    }
}
