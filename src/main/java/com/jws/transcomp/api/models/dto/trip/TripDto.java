package com.jws.transcomp.api.models.dto.trip;

import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.models.dto.client.ClientDto;
import com.jws.transcomp.api.models.dto.company.CompanyDto;
import com.jws.transcomp.api.models.dto.employee.EmployeeDto;
import com.jws.transcomp.api.models.dto.vehicle.VehicleDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class TripDto implements Serializable {
    private Long id;
    // @NotNull
    private String startingPoint;
    // @NotNull
    private String endingPoint;
    ///  @NotNull
    private Date departure;
    // @NotNull
    private Date arrival;
    // @NotNull
    private BigDecimal basePrice;
    //@NotNull
    private BigDecimal totalPrice;
    // @NotNull
    private TripType type;
    private short peopleOnboard;
    private int cargoSize;
    private EmployeeDto driver;
    private VehicleDto vehicle;
    private CompanyDto company;
    private Set<ClientDto> clients;

    public TripDto() {

    }

    public TripDto(Long id, String startingPoint, String endingPoint, Date departure, Date arrival, BigDecimal basePrice, BigDecimal totalPrice, TripType type, short peopleOnboard, int cargoSize, EmployeeDto driver, VehicleDto vehicle, CompanyDto company, Set<ClientDto> clients) {
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
        this.company = company;
        this.clients = clients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getEndingPoint() {
        return endingPoint;
    }

    public void setEndingPoint(String endingPoint) {
        this.endingPoint = endingPoint;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public TripType getType() {
        return type;
    }

    public void setType(TripType type) {
        this.type = type;
    }

    public short getPeopleOnboard() {
        return peopleOnboard;
    }

    public void setPeopleOnboard(short peopleOnboard) {
        this.peopleOnboard = peopleOnboard;
    }

    public int getCargoSize() {
        return cargoSize;
    }

    public void setCargoSize(int cargoSize) {
        this.cargoSize = cargoSize;
    }

    public EmployeeDto getDriver() {
        return driver;
    }

    public void setDriver(EmployeeDto driver) {
        this.driver = driver;
    }

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public Set<ClientDto> getClients() {
        return clients;
    }

    public void setClients(Set<ClientDto> clients) {
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
                Objects.equals(this.company, entity.company) &&
                Objects.equals(this.clients, entity.clients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startingPoint, endingPoint, departure, arrival, basePrice, totalPrice, type, peopleOnboard, cargoSize, driver, vehicle, company, clients);
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
                "company = " + company + ", " +
                "clients = " + clients + ")";
    }


}
