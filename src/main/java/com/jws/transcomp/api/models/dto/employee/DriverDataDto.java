package com.jws.transcomp.api.models.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DriverDataDto {
    private Long id;
    private String name;
    private int tripCount;
    private BigDecimal revenueCount;
}
