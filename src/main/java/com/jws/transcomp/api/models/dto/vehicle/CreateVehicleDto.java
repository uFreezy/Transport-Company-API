package com.jws.transcomp.api.models.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateVehicleDto {
    @NotNull
    @Size(min = 3, max = 10)
    private String make;
    @NotNull
    private String model;
    @NotNull
    @JsonProperty("fuel_type")
    private FuelType fuelType;
    @NotNull
    @Range(min = 1, max = 100)
    @JsonProperty("people_capacity")
    private short peopleCapacity;
    @NotNull
    @Range(min = 1, max = Integer.MAX_VALUE)
    @JsonProperty("cargo_capacity_kg")
    private short cargoCapacityKg;
    @NotNull
    @JsonProperty("required_licenses")
    private Set<LiscenceType> requiredLicenses;

    public CreateVehicleDto() {
    }

    public CreateVehicleDto(String make, String model, FuelType fuelType, short peopleCapacity, short cargoCapacityKg, Set<LiscenceType> requiredLicenses) {
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.peopleCapacity = peopleCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.requiredLicenses = requiredLicenses;
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

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        if (fuelType != null)
            this.fuelType = parseFuelType(fuelType);
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

    public void setCargoCapacityKg(short cargoCapacityKg) {
        this.cargoCapacityKg = cargoCapacityKg;
    }

    public Set<LiscenceType> getRequiredLicenses() {
        return requiredLicenses;
    }

    public void setRequiredLicenses(Set<LiscenceType> requiredLicenses) {
        this.requiredLicenses = requiredLicenses;
    }

    private FuelType parseFuelType(String fuelType) {
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

    private Set<LiscenceType> parseLicences(List<String> licencesRaw) {
        Set<LiscenceType> licences = new HashSet<>();
        for (String licence : licencesRaw) {
            licences.add(LiscenceType.valueOf(licence));
        }

        return licences;
    }
}
