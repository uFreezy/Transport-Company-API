package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Trip;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> getById(Long id);

    @Override
    <S extends Trip> List<S> findAll(Example<S> example);


    Page<Trip> findAllByCompanyId(Long companyId, Pageable pageable);

    Page<Trip> findAllByCompanyIdAndEndingPoint(Long comapnyId, String endingPoint, Pageable pageable);

}
