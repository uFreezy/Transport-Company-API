package com.jws.transcomp.api.models.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LicenseType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
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


    public EditVehicleDto() {
    }

    public EditVehicleDto(Long id, String make,
                          String model, String fuelType,
                          short peopleCapacity, int cargoCapacityKg,
                          Set<String> requiredLicenses) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.peopleCapacity = peopleCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.requiredLicenses = requiredLicenses;
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

    Set<LicenseType> parseLicences() {
        Set<LicenseType> licences = new HashSet<>();
        for (String licence : this.requiredLicenses) {
            licences.add(LicenseType.valueOf(licence));
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


}
