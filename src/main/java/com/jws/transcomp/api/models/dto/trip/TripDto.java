package com.jws.transcomp.api.models.dto.trip;

import com.jws.transcomp.api.models.base.TripType;
import com.jws.transcomp.api.models.dto.client.ClientDtoShort;
import com.jws.transcomp.api.models.dto.employee.EmployeeDto;
import com.jws.transcomp.api.models.dto.vehicle.VehicleDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
}
