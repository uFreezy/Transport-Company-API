package com.jws.transcomp.api.models.dto.employee;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DriverDataDto {
    private Long id;
    private String name;
    private int tripCount;
    private BigDecimal revenueCount;

    public DriverDataDto(Long id, String name, int tripCount, BigDecimal revenueCount) {
        this.id = id;
        this.name = name;
        this.tripCount = tripCount;
        this.revenueCount = revenueCount;
    }
}
