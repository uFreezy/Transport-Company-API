package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.dto.employee.DriverDataDto;

import java.math.BigDecimal;
import java.util.List;

public class CompanyReportDto {
    private int totalTrips;
    private BigDecimal totalRevenue;
    private List<DriverDataDto> driverData;

    public CompanyReportDto(int totalTrips, BigDecimal totalRevenue, List<DriverDataDto> driverData) {
        this.totalTrips = totalTrips;
        this.totalRevenue = totalRevenue;
        this.driverData = driverData;
    }


    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<DriverDataDto> getDriverData() {
        return driverData;
    }

    public void setDriverData(List<DriverDataDto> driverData) {
        this.driverData = driverData;
    }
}
