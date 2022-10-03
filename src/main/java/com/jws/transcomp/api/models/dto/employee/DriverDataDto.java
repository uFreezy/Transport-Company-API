package com.jws.transcomp.api.models.dto.employee;

import java.math.BigDecimal;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTripCount() {
        return tripCount;
    }

    public void setTripCount(int tripCount) {
        this.tripCount = tripCount;
    }

    public BigDecimal getRevenueCount() {
        return revenueCount;
    }

    public void setRevenueCount(BigDecimal revenueCount) {
        this.revenueCount = revenueCount;
    }
}
