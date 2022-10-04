package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.dto.employee.DriverDataDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CompanyReportDto {
    private int totalTrips;
    private BigDecimal totalRevenue;
    private List<DriverDataDto> driverData;

    public CompanyReportDto(int totalTrips, BigDecimal totalRevenue, List<DriverDataDto> driverData) {
        this.totalTrips = totalTrips;
        this.totalRevenue = totalRevenue;
        this.driverData = driverData;
    }
}
