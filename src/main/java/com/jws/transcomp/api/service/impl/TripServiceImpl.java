package com.jws.transcomp.api.service.impl;

import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.models.dto.trip.TripDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import com.jws.transcomp.api.repository.CompanyRepository;
import com.jws.transcomp.api.repository.TripRepository;
import com.jws.transcomp.api.service.base.TripService;
import com.jws.transcomp.api.service.specs.TripSpecifications;
import com.jws.transcomp.api.util.PageRequestUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public Trip save(Trip trip) {
        Company company = trip.getCompany();
        for (Client client : trip.getClients()) {
            company.addClient(client);
        }
        company.addClients(trip.getClients());

        Trip trp = this.tripRepository.save(trip);
        this.companyRepository.save(company);

        return trp;
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
                .orElseThrow(() -> new EntityNotFoundException("Invalid trip id!"));
    }


    @Override
    public List<Trip> getTripsByType(TripType type) {
        Specification<Trip> spec = Specification.where(TripSpecifications.hasType(type));

        return this.tripRepository.findAll(spec);
    }

    @Override
    public List<Trip> getTripsForDestination(String destName) {
        Specification<Trip> spec = Specification.where(TripSpecifications.hasDestination(destName));


        return this.tripRepository.findAll(spec);
    }

    @Override
    public List<Trip> getAll() {
        return this.tripRepository.findAll();
    }

    @Override
    public PaginatedResponse<TripDto> filterTrips(Long companyId, String destination, String sortBy, Pageable pageable) {
        if (companyId == null) {
            throw new IllegalArgumentException("Company id is empty.");
        }

        if (sortBy != null) {
            pageable = PageRequestUtil.createPageRequest(pageable, sortBy);
        }

        Specification<Trip> spec = Specification.where(TripSpecifications.hasCompanyId(companyId));

        if (destination != null) {
            spec = spec.and(TripSpecifications.hasDestination(destination));
        }

        Page<Trip> trips = this.tripRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(PaginatedResponse.mapDto(trips.getContent(), TripDto.class),
                trips.getTotalElements(),
                trips.getTotalPages());
    }

    @Override
    public long count() {
        return this.tripRepository.count();
    }
}
