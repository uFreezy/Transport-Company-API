package com.jws.transcomp.api.models;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "companies")
public class Company {

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Trip> trips = new LinkedHashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Employee> employees = new LinkedHashSet<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new LinkedHashSet<>();
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Client> clients;
    @Formula("(select sum(t.totalprice) from trips t where t.company_id = id)")
    private BigDecimal revenue;

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public Company(String name, Set<Employee> employees) {
        this.name = name;
        this.employees = employees;
    }

    public Company(String name, Set<Employee> employees, Set<Vehicle> vehicles) {
        this(name, employees);
        this.vehicles = vehicles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Trip> getTrips() {
        return trips;
    }

    public BigDecimal getRevenue() {
        return this.revenue;
    }


    public BigDecimal getRevenue(Date from, Date to) {
        BigDecimal revenueCalc = new BigDecimal(0);
        List<Trip> tripsFiltered = this.trips.stream().filter(t -> t.getDeparture().after(from) && t.getDeparture().before(to)).collect(Collectors.toList());
        for (Trip t : tripsFiltered) {
            revenueCalc = revenueCalc.add(t.getTotalPrice());
        }

        return revenueCalc;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
