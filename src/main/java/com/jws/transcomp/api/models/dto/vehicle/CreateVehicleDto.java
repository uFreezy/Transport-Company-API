package com.jws.transcomp.api.models.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
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
    private int cargoCapacityKg;
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

    public void setFuelType(String fuelType) {
        if (fuelType != null)
            this.fuelType = parseFuelType(fuelType);
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
}
