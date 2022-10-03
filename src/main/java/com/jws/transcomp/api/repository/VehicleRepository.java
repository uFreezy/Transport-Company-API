package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> getById(Long id);

    @Query(value = "SELECT * FROM vehicles v WHERE v.company_id = ?1", nativeQuery = true)
    List<Vehicle> findAllByCompany(Long id);
}
