package com.jws.transcomp.api.service.base;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.base.FuelType;

import java.util.List;

public interface VehicleService {
    Vehicle save(Vehicle vehicle);

    boolean any();

    void delete(Long id);

    Vehicle findById(Long id);

    List<Vehicle> getByCompany(Company company);

    List<Vehicle> getByMake(String makeName);

    List<Vehicle> getByFuelType(FuelType fuelType);

    List<Vehicle> getAll();
}
