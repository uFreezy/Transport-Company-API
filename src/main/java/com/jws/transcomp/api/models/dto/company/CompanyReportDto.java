package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.dto.employee.DriverDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CompanyReportDto {
    private int totalTrips;
    private BigDecimal totalRevenue;
    private List<DriverDataDto> driverData;
}
