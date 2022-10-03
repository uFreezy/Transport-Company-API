package com.jws.transcomp.api.models.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;
import org.hibernate.validator.constraints.Range;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class EditVehicleDto {
    @NotNull
    private Long id;

    @Size(min = 3, max = 10)
    private String make;

    @Size(min = 3, max = 10)
    private String model;

    @JsonProperty("fuel_type")
    private String fuelType;

    @Range(min = 1, max = 100)
    @JsonProperty("people_capacity")
    private short peopleCapacity;

    @Range(min = 1, max = Integer.MAX_VALUE)
    @JsonProperty("cargo_capacity_kg")
    private int cargoCapacityKg;

    @JsonProperty("required_licenses")
    private Set<String> requiredLicenses;

    private Company company;

    public EditVehicleDto() {
    }

    public EditVehicleDto(Long id, String make, String model, String fuelType, short peopleCapacity, int cargoCapacityKg, Set<String> requiredLicenses, Company company) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.peopleCapacity = peopleCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.requiredLicenses = requiredLicenses;
        this.company = company;
    }

    FuelType parseFuelType() {
        switch (fuelType.toLowerCase()) {
            case "petrol":
                return FuelType.PETROL;
            case "diesel":
                return FuelType.DIESEL;
            case "electric":
                return FuelType.ELECTRIC_VEHICLE;
            case "hybrid":
                return FuelType.HYBRID;
            default:
                throw new IllegalArgumentException("Not a valid fuel type!");
        }
    }

    Set<LiscenceType> parseLicences() {
        Set<LiscenceType> licences = new HashSet<>();
        for (String licence : this.requiredLicenses) {
            licences.add(LiscenceType.valueOf(licence));
        }

        return licences;
    }

    public void mapToEntity(Vehicle destination) {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(this.getClass(), Vehicle.class).addMappings(mp -> {
            mp.map(EditVehicleDto::parseFuelType, Vehicle::setFuelType);
            mp.map(EditVehicleDto::parseLicences, Vehicle::setRequiredLicenses);
        });

        mapper.map(this, destination);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public short getPeopleCapacity() {
        return peopleCapacity;
    }

    public void setPeopleCapacity(short peopleCapacity) {
        this.peopleCapacity = peopleCapacity;
    }

    public int getCargoCapacityKg() {
        return cargoCapacityKg;
    }

    public void setCargoCapacityKg(int cargoCapacityKg) {
        this.cargoCapacityKg = cargoCapacityKg;
    }

    public Set<String> getRequiredLicenses() {
        return requiredLicenses;
    }

    public void setRequiredLicenses(Set<String> requiredLicenses) {
        this.requiredLicenses = requiredLicenses;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
