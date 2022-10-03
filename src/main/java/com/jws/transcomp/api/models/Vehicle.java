package com.jws.transcomp.api.models;

import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String make;
    @NotNull
    private String model;
    @NotNull
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    @NotNull
    private short peopleCapacity;
    @NotNull
    private int cargoCapacityKg;

    @ElementCollection(targetClass = LiscenceType.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)

    private Set<LiscenceType> requiredLiscences;


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "company_id")
    private Company company;

    public Vehicle() {
    }

    public Vehicle(String make, String model, FuelType fuelType, short peopleCapacity, int cargoCapacityKg, Set<LiscenceType> requiredLicenses, Company company) {
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.peopleCapacity = peopleCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.requiredLiscences = requiredLicenses;
        this.company = company;
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
        return requiredLiscences;
    }

    public void setRequiredLicenses(Set<LiscenceType> requiredLiscences) {
        this.requiredLiscences = requiredLiscences;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", fuelType=" + fuelType +
                ", peopleCapacity=" + peopleCapacity +
                ", cargoCapacityKg=" + cargoCapacityKg +
                ", requiredLiscences=" + requiredLiscences +
                '}';
    }
}
