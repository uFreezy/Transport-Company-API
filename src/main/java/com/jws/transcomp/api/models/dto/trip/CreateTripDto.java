package com.jws.transcomp.api.models.dto.trip;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.Vehicle;
import com.jws.transcomp.api.models.base.TripType;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreateTripDto {
    @NotNull
    @Size(min = 3, max = 50)
    @JsonProperty("starting_point")
    private String startingPoint;

    @NotNull
    @Size(min = 3, max = 50)
    @JsonProperty("ending_point")
    private String endingPoint;
    @NotNull
    private Date departure;
    @NotNull
    private Date arrival;

    @Min(1)
    @NotNull
    @JsonProperty("base_price")
    private BigDecimal basePrice;

    @Min(1)
    @NotNull
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TripType type;

    @NotNull
    @Min(value = 1)
    @JsonProperty("cargo_size")
    private int cargoSize;

    @NotNull
    @JsonProperty("driver_id")
    private Long driverId;

    private Employee driver;

    @NotNull
    @JsonProperty("vehicle_id")
    private Long vehicleId;

    private Vehicle vehicle;

    private Company company;

    @AssertTrue(message = "Trip dates cannot be in the past.")
    private boolean isDatesArentInThePast() {
        return this.departure.after(new Date()) || this.arrival.after(new Date());
    }

    @AssertTrue(message = "Departure date must be before arrival date ")
    private boolean isDepartureBeforeArrival() {
        return this.departure.before(this.arrival);
    }

    public Trip mapToEntity() {
        Trip trip = new Trip();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.map(this, trip);

        return trip;
    }
}
