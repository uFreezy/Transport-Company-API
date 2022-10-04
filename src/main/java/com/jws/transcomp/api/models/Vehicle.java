package com.jws.transcomp.api.models;

import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
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

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Trip> trips;

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

    public Set<LiscenceType> getRequiredLicenses() {
        return requiredLiscences;
    }

    public void setRequiredLicenses(Set<LiscenceType> requiredLiscences) {
        this.requiredLiscences = requiredLiscences;
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
