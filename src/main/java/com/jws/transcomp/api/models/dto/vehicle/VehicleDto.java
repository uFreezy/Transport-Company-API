package com.jws.transcomp.api.models.dto.vehicle;

import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LicenseType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
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

    private Set<LicenseType> requiredLicenses;

    public VehicleDto() {
    }

    public VehicleDto(Long id, String make, String model, FuelType fuelType, short peopleCapacity, int cargoCapacityKg, Set<LicenseType> requiredLicenses) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.peopleCapacity = peopleCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.requiredLicenses = requiredLicenses;
    }
}
