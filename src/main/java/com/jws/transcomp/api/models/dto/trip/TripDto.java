package com.jws.transcomp.api.models.dto.trip;

import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.models.dto.client.ClientDtoShort;
import com.jws.transcomp.api.models.dto.employee.EmployeeDto;
import com.jws.transcomp.api.models.dto.vehicle.VehicleDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Data
public class TripDto implements Serializable {
    private Long id;
    private String startingPoint;
    private String endingPoint;
    private Date departure;
    private Date arrival;
    private BigDecimal basePrice;
    private BigDecimal totalPrice;
    private TripType type;
    private short peopleOnboard;
    private int cargoSize;
    private EmployeeDto driver;
    private VehicleDto vehicle;
    private Set<ClientDtoShort> clients;

    public TripDto() {
    }

    public TripDto(Long id, String startingPoint, String endingPoint, Date departure, Date arrival, BigDecimal basePrice, BigDecimal totalPrice, TripType type, short peopleOnboard, int cargoSize, EmployeeDto driver, VehicleDto vehicle, Set<ClientDtoShort> clients) {
        this.id = id;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.departure = departure;
        this.arrival = arrival;
        this.basePrice = basePrice;
        this.totalPrice = totalPrice;
        this.type = type;
        this.peopleOnboard = peopleOnboard;
        this.cargoSize = cargoSize;
        this.driver = driver;
        this.vehicle = vehicle;
        this.clients = clients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripDto entity = (TripDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.startingPoint, entity.startingPoint) &&
                Objects.equals(this.endingPoint, entity.endingPoint) &&
                Objects.equals(this.departure, entity.departure) &&
                Objects.equals(this.arrival, entity.arrival) &&
                Objects.equals(this.basePrice, entity.basePrice) &&
                Objects.equals(this.totalPrice, entity.totalPrice) &&
                Objects.equals(this.type, entity.type) &&
                Objects.equals(this.peopleOnboard, entity.peopleOnboard) &&
                Objects.equals(this.cargoSize, entity.cargoSize) &&
                Objects.equals(this.driver, entity.driver) &&
                Objects.equals(this.vehicle, entity.vehicle) &&
                Objects.equals(this.clients, entity.clients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startingPoint, endingPoint, departure, arrival, basePrice, totalPrice, type, peopleOnboard, cargoSize, driver, vehicle, clients);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "startingPoint = " + startingPoint + ", " +
                "endingPoint = " + endingPoint + ", " +
                "departure = " + departure + ", " +
                "arrival = " + arrival + ", " +
                "basePrice = " + basePrice + ", " +
                "totalPrice = " + totalPrice + ", " +
                "type = " + type + ", " +
                "peopleOnboard = " + peopleOnboard + ", " +
                "cargoSize = " + cargoSize + ", " +
                "driver = " + driver + ", " +
                "vehicle = " + vehicle + ", " +
                "clients = " + clients + ")";
    }
}
