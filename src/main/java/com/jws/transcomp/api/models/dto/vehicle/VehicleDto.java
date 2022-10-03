package com.jws.transcomp.api.models.dto.vehicle;

import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

public class VehicleDto implements Serializable {
    private Long id;
    @NotNull
    private String make;
    @NotNull
    private String model;
    @NotNull
    private FuelType fuelType;
    @NotNull
    private short peopleCapacity;
    @NotNull
    private int cargoCapacityKg;

    private Set<LiscenceType> requiredLicenses;

    public VehicleDto() {
    }

    public VehicleDto(Long id, String make, String model, FuelType fuelType, short peopleCapacity, int cargoCapacityKg, Set<LiscenceType> requiredLicenses) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.peopleCapacity = peopleCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.requiredLicenses = requiredLicenses;
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

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
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

    public Set<LiscenceType> getRequiredLicenses() {
        return requiredLicenses;
    }

    public void setRequiredLicenses(Set<LiscenceType> requiredLicenses) {
        this.requiredLicenses = requiredLicenses;
    }
}
