package com.jws.transcomp.api.service;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.repository.VehicleRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void save(Vehicle vehicle) {
        this.vehicleRepository.save(vehicle);
    }

    @Override
    public boolean any() {
        return this.vehicleRepository.count() > 0;
    }

    @Override
    public void delete(Long id) {
        Vehicle vh = this.vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle with that id doesn't exist."));
        this.vehicleRepository.delete(vh);
    }

    @Override
    public Vehicle findById(Long id) {
        return this.vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle id."));
    }

    @Override
    public List<Vehicle> getByCompany(Company company) {
        return this.vehicleRepository.findAllByCompany(company.getId());
    }

    @Override
    public List<Vehicle> getByMake(String makeName) {
        Vehicle example = new Vehicle();
        example.setMake(makeName);

        return this.vehicleRepository.findAll(Example.of(example));
    }

    @Override
    public List<Vehicle> getByFuelType(FuelType fuelType) {
        Vehicle example = new Vehicle();
        example.setFuelType(fuelType);

        return this.vehicleRepository.findAll(Example.of(example));
    }

    @Override
    public List<Vehicle> getAll() {
        return this.vehicleRepository.findAll();
    }
}
