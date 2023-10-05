package com.jws.transcomp.api.models;

import com.jws.transcomp.api.models.base.FuelType;
import com.jws.transcomp.api.models.base.LiscenceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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

    @Getter
    @ElementCollection(targetClass = LiscenceType.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)

    private Set<LiscenceType> requiredLicenses;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Trip> trips;

    public Vehicle(String make, String model, FuelType fuelType, short peopleCapacity, int cargoCapacityKg, Set<LiscenceType> requiredLicenses, Company company) {
        this.make = make;
        this.model = model;
        this.fuelType = fuelType;
        this.peopleCapacity = peopleCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
        this.requiredLicenses = requiredLicenses;
        this.company = company;
    }

    public void setRequiredLicenses(Set<LiscenceType> requiredLiscences) {
        this.requiredLicenses = requiredLiscences;
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
                ", requiredLiscences=" + requiredLicenses +
                '}';
    }
}
