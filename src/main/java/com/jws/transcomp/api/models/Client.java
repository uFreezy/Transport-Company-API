package com.jws.transcomp.api.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    @ManyToMany(mappedBy = "clients")
    private Set<Trip> trips = new HashSet<>();

    @ManyToMany(mappedBy = "paidClients")
    private Set<Trip> paidTrips = new HashSet<>();

    @ManyToMany(mappedBy = "clients")
    private Set<Company> companies = new HashSet<>();


    public Client() {
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    public Set<Trip> getPaidTrips() {
        return paidTrips;
    }

    public void setPaidTrips(Set<Trip> paidTrips) {
        this.paidTrips = paidTrips;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public void addCompany(Company cmp) {
        this.companies.add(cmp);
    }

    public boolean removeCompany(Company cmp) {
        return this.companies.remove(cmp);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
