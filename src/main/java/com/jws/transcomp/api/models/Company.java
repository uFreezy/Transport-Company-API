package com.jws.transcomp.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Trip> trips = new LinkedHashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Employee> employees = new LinkedHashSet<>();
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Client> clients = new HashSet<>();

    @Formula("(select sum(t.totalprice) from trips t where t.company_id = id)")
    private BigDecimal revenue;

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

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    public BigDecimal getRevenue(Date from, Date to) {
        BigDecimal revenueCalc = new BigDecimal(0);
        List<Trip> tripsFiltered = this.trips.stream()
                .filter(t -> t.getDeparture().after(from) && t.getDeparture().before(to)).collect(Collectors.toList());

        for (Trip t : tripsFiltered) {
            revenueCalc = revenueCalc.add(t.getTotalPrice());
        }

        return revenueCalc;
    }

    public void addClient(Client client) {
        this.clients.add(client);
    }

    public void addClients(Set<Client> clients) {
        this.clients.addAll(clients);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Company company = (Company) o;
        return id != null && Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
