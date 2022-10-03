package com.jws.transcomp.api.service;


import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TripService {
    void save(Trip trip);

    boolean any();

    boolean delete(Trip trip);

    Trip findById(long id);

    List<Trip> getTripsByType(TripType type);

    List<Trip> getTripsForDestination(String destName);

    List<Trip> getAll();

    PaginatedResponse filterTrips(Long companyId, String destination, String sortBy, Pageable pageable);

    long count();
}
