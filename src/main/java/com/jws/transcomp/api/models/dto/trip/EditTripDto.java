package com.jws.transcomp.api.models.dto.trip;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.Trip;
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

    @Min(1)
    @JsonProperty("base_price")
    private BigDecimal basePrice;

    @Min(1)
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private TripType type;

    @Min(value = 1)
    @JsonProperty("cargo_size")
    private int cargoSize;

    @JsonProperty("driver_id")
    private Long driverId;

    @JsonProperty("vehicle_id")
    private Long vehicleId;

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
}
