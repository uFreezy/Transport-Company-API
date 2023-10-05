package com.jws.transcomp.api.models.dto.client;

import com.jws.transcomp.api.models.Trip;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ClientDto implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 3, max = 30)
    private String name;
    private List<Long> trips;
    private List<Long> paidTrips;

    public ClientDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = new ArrayList<>();

        trips.forEach(trip -> this.trips.add(trip.getId()));
    }

    public void setPaidTrips(List<Trip> paidTrips) {
        this.paidTrips = new ArrayList<>();

        paidTrips.forEach(t -> this.paidTrips.add(t.getId()));
    }
}
