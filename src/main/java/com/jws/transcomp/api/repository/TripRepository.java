package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAll(Specification<Trip> spec);

    Page<Trip> findAll(Specification<Trip> spec, Pageable pageable);

}
