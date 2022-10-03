package com.jws.transcomp.api.models.dto.trip;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.base.TripType;
import org.modelmapper.ModelMapper;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

public class EditTripDto {
    @NotNull
    private Long id;

    @Size(min = 3, max = 50)
    @JsonProperty("starting_point")
    private String startingPoint;

    @Size(min = 3, max = 50)
    @JsonProperty("ending_point")
    private String endingPoint;
    private Date departure;

    private Date arrival;

    @JsonProperty("base_price")
    private BigDecimal basePrice;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private TripType type;

    @Min(value = 1)
    @JsonProperty("cargo_size")
    private int cargoSize;


    @JsonProperty("driver_id")
    private Long driverId;

    private Employee driver;


    @JsonProperty("vehicle_id")
    private Long vehicleId;

    private Vehicle vehicle;

    private Company company;


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

    public int getCargoSize() {
        return cargoSize;
    }

    public void setCargoSize(int cargoSize) {
        this.cargoSize = cargoSize;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @AssertTrue(message = "Trip dates cannot be in the past.")
    public boolean datesArentInThePast() {
        return this.departure.after(new Date()) || this.arrival.after(new Date());
    }

    @AssertTrue(message = "Departure date must be before arrival date ")
    public boolean isDepartureBeforeArrival() {
        return this.departure.before(this.arrival);
    }

    public void mapToEntity(Trip trip) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.map(this, trip);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
