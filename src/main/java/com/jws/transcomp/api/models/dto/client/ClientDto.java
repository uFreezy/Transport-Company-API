package com.jws.transcomp.api.models.dto.client;

import com.jws.transcomp.api.models.Trip;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientDto implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 3, max = 30)
    private String name;
    private List<Long> trips;
    private List<Long> paidTrips;

    public ClientDto() {
    }

    public ClientDto(Long id, String name) {
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

    public List<Long> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = new ArrayList<>();

        trips.forEach(trip -> this.trips.add(trip.getId()));
    }

    public List<Long> getPaidTrips() {
        return paidTrips;
    }

    public void setPaidTrips(List<Trip> paidTrips) {
        this.paidTrips = new ArrayList<>();

        paidTrips.forEach(t -> this.paidTrips.add(t.getId()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDto entity = (ClientDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }
}
